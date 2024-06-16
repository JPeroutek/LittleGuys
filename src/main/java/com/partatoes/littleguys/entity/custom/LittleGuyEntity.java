package com.partatoes.littleguys.entity.custom;

import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.entity.goal.BoardLittleHorseGoal;
import com.partatoes.littleguys.item.ModItems;
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
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LittleGuyEntity extends PathAwareEntity {

    private static final TrackedData<Byte> COLOR;
    private static final Map<DyeColor, float[]> COLORS;

    public LittleGuyEntity(EntityType<? extends LittleGuyEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.add(2, new BoardLittleHorseGoal(this));
        this.goalSelector.add(3, new WanderAroundGoal(this, .3));

        this.targetSelector.add(0, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(1, new ActiveTargetGoal<LittleGuyEntity>(this, LittleGuyEntity.class, 1, true, true, this::canLittleGuyAttackTarget));
    }

    public static DefaultAttributeContainer.Builder createLittleGuyAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, .5);
    }
    private static float[] getDyedColor(DyeColor color) {
        if (color == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] fs = color.getColorComponents();
            float f = 0.75F;
            return new float[]{fs[0] * 0.75F, fs[1] * 0.75F, fs[2] * 0.75F};
        }
    }
    public static float[] getRgbColor(DyeColor dyeColor) {
        return COLORS.get(dyeColor);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, (byte) 0);
    }

    public DyeColor getColor() {
        return DyeColor.byId((Byte)this.dataTracker.get(COLOR) & 15);
    }

    public void setColor(DyeColor color) {
        byte b = (Byte)this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte)(b & 240 | color.getId() & 15));
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        Item drop = ModItems.LITTLEGUY_COLORS.getOrDefault(this.getColor(), ModItems.LITTLEGUY_ITEM);

        this.dropItem(drop);
    }

    public boolean canLittleGuyAttackTarget(@Nullable LivingEntity target){
        if (target instanceof LittleGuyEntity) {
            return ((LittleGuyEntity) target).getColor() != this.getColor();
        }
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Color", (byte) this.getColor().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setColor(DyeColor.byId(nbt.getByte("Color")));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setColor(this.getColor());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    static {
        COLOR = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BYTE);
        COLORS = Arrays.stream(DyeColor.values())
                .collect(Collectors.toMap((color) -> color, LittleGuyEntity::getDyedColor));
    }
}
