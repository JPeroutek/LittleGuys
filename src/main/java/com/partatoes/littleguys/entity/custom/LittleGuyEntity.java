package com.partatoes.littleguys.entity.custom;

import com.partatoes.littleguys.entity.goal.BoardLittleHorseGoal;
import com.partatoes.littleguys.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import static com.partatoes.littleguys.entity.ModEntities.COLOR_LITTLEGUY_BIMAP;
import static com.partatoes.littleguys.entity.ModEntities.LITTLEGUY_ENTITY;

public class LittleGuyEntity extends PathfinderMob {

    private static final EntityDataAccessor<Byte> COLOR;
    private static final EntityDataAccessor<Boolean> IS_NEUTRAL;

    public LittleGuyEntity(EntityType<? extends LittleGuyEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(2, new BoardLittleHorseGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, stack -> stack.is(Items.CLAY_BALL), false, 3));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, .3));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LittleGuyEntity.class, 1, true, true, this::canLittleGuyAttackTarget));
    }

    public static AttributeSupplier.Builder createLittleGuyAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6)
                .add(Attributes.MOVEMENT_SPEED, .35)
                .add(Attributes.ATTACK_DAMAGE, .5)
                .add(Attributes.TEMPT_RANGE, 5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, (byte) COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE).getId());
        builder.define(IS_NEUTRAL, this.getType().equals(LITTLEGUY_ENTITY));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        DyeColor dyeColor = itemStack.get(DataComponents.DYE);
        if (itemStack.getItem() instanceof DyeItem && dyeColor != null && dyeColor != this.getColor()) {
            if (this.level() instanceof ServerLevel) {
                itemStack.shrink(1);
                this.setColor(dyeColor);
                this.setIsNeutral(false);
                return InteractionResult.SUCCESS_SERVER;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.getItem() instanceof BlockItem blockItem)  {
            if (blockItem.getBlock() instanceof SpongeBlock || blockItem.getBlock() instanceof WetSpongeBlock) {
                if (this.level() instanceof ServerLevel) {
                    this.setColor(DyeColor.WHITE);
                    this.setIsNeutral(true);
                    return InteractionResult.SUCCESS_SERVER;
                } else {
                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(COLOR));
    }

    public void setColor(DyeColor color) {
        this.entityData.set(COLOR, (byte) color.getId());
    }

    public boolean isNeutral() {
        return this.entityData.get(IS_NEUTRAL);
    }

    public void setIsNeutral(Boolean isNeutral) {
        this.entityData.set(IS_NEUTRAL, isNeutral);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        Item drop;
        if (isNeutral()) {
            drop = ModItems.LITTLEGUY_ITEM;
        } else {
            drop = ModItems.LITTLEGUY_COLORS.getOrDefault(this.getColor(), ModItems.LITTLEGUY_ITEM);
        }

        Level w = this.level();
        if (w instanceof ServerLevel serverWorld) {
            this.spawnAtLocation(serverWorld, drop);
        }
    }

    public boolean canLittleGuyAttackTarget(@Nullable LivingEntity target, ServerLevel serverWorld){
        if ((target instanceof LittleGuyEntity lgTarget) && !this.isNeutral()) {
            return !lgTarget.isNeutral() &&  (lgTarget.getColor() != this.getColor());
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putString("Color", this.getColor().getName());
        view.putBoolean("isNeutral", this.isNeutral());
    }

    @Override
    public void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.setColor(DyeColor.byName(view.getStringOr("Color", "White"), DyeColor.WHITE));
        this.setIsNeutral(view.getBooleanOr("isNeutral", true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        this.setColor(COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    static {
        COLOR = SynchedEntityData.defineId(LittleGuyEntity.class, EntityDataSerializers.BYTE);

        IS_NEUTRAL = SynchedEntityData.defineId(LittleGuyEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
