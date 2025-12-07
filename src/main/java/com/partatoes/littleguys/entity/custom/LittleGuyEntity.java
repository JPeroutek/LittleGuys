package com.partatoes.littleguys.entity.custom;

import com.partatoes.littleguys.entity.goal.BoardLittleHorseGoal;
import com.partatoes.littleguys.item.ModItems;
import net.minecraft.block.SpongeBlock;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import static com.partatoes.littleguys.entity.ModEntities.COLOR_LITTLEGUY_BIMAP;
import static com.partatoes.littleguys.entity.ModEntities.LITTLEGUY_ENTITY;

public class LittleGuyEntity extends PathAwareEntity {

    private static final TrackedData<Byte> COLOR;
    private static final TrackedData<Boolean> IS_NEUTRAL;

    public LittleGuyEntity(EntityType<? extends LittleGuyEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.add(2, new BoardLittleHorseGoal(this));
        this.goalSelector.add(3, new TemptGoal(this, 1.2, stack -> stack.isOf(Items.CLAY_BALL), false, 3));
        this.goalSelector.add(4, new WanderAroundGoal(this, .3));

        this.targetSelector.add(0, new RevengeGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, LittleGuyEntity.class, 1, true, true, this::canLittleGuyAttackTarget));
    }

    public static DefaultAttributeContainer.Builder createLittleGuyAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 6)
                .add(EntityAttributes.MOVEMENT_SPEED, .35)
                .add(EntityAttributes.ATTACK_DAMAGE, .5)
                .add(EntityAttributes.TEMPT_RANGE, 5);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, (byte) COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE).getIndex());
        builder.add(IS_NEUTRAL, this.getType().equals(LITTLEGUY_ENTITY));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof DyeItem dyeItem && dyeItem.getColor() != this.getColor()) {
            if (this.getEntityWorld() instanceof ServerWorld) {
                itemStack.decrement(1);
                this.setColor(dyeItem.getColor());
                this.setIsNeutral(false);
                return ActionResult.SUCCESS_SERVER;
            } else {
                return ActionResult.CONSUME;
            }
        } else if (itemStack.getItem() instanceof BlockItem blockItem)  {
            if (blockItem.getBlock() instanceof SpongeBlock || blockItem.getBlock() instanceof WetSpongeBlock) {
                if (this.getEntityWorld() instanceof ServerWorld) {
                    this.setColor(DyeColor.WHITE);
                    this.setIsNeutral(true);
                    return ActionResult.SUCCESS_SERVER;
                } else {
                    return ActionResult.CONSUME;
                }
            }
        }
        return super.interactMob(player, hand);
    }

    public DyeColor getColor() {
        return DyeColor.byIndex(this.dataTracker.get(COLOR));
    }

    public void setColor(DyeColor color) {
        this.dataTracker.set(COLOR, (byte) color.getIndex());
    }

    public boolean isNeutral() {
        return this.dataTracker.get(IS_NEUTRAL);
    }

    public void setIsNeutral(Boolean isNeutral) {
        this.dataTracker.set(IS_NEUTRAL, isNeutral);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        Item drop;
        if (isNeutral()) {
            drop = ModItems.LITTLEGUY_ITEM;
        } else {
            drop = ModItems.LITTLEGUY_COLORS.getOrDefault(this.getColor(), ModItems.LITTLEGUY_ITEM);
        }

        World w = this.getEntityWorld();
        if (w instanceof ServerWorld serverWorld) {
            this.dropItem(serverWorld, drop);
        }
    }

    public boolean canLittleGuyAttackTarget(@Nullable LivingEntity target, ServerWorld serverWorld){
        if ((target instanceof LittleGuyEntity lgTarget) && !this.isNeutral()) {
            return !lgTarget.isNeutral() &&  (lgTarget.getColor() != this.getColor());
        }
        return false;
    }

    @Override
    public void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putString("Color", this.getColor().getId());
        view.putBoolean("isNeutral", this.isNeutral());
    }

    @Override
    public void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setColor(DyeColor.byId(view.getString("Color", "White"), DyeColor.WHITE));
        this.setIsNeutral(view.getBoolean("isNeutral", true));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setColor(COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    static {
        COLOR = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BYTE);

        IS_NEUTRAL = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
