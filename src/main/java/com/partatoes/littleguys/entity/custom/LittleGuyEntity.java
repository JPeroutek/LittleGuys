package com.partatoes.littleguys.entity.custom;

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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.partatoes.littleguys.entity.ModEntities.COLOR_LITTLEGUY_BIMAP;
import static com.partatoes.littleguys.entity.ModEntities.LITTLEGUY_ENTITY;

public class LittleGuyEntity extends PathAwareEntity {

    private static final TrackedData<Byte> COLOR;
    private static final TrackedData<Boolean> IS_NEUTRAL;
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

        this.targetSelector.add(0, new RevengeGoal(this));
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
            int colorInt = color.getEntityColor();
            float colorScaler = 0.75f;

            return new float[]{
                    ColorHelper.Argb.getRed(colorInt) * colorScaler,
                    ColorHelper.Argb.getGreen(colorInt) * colorScaler,
                    ColorHelper.Argb.getBlue(colorInt) * colorScaler
            };
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, (byte) COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE).getId());
        builder.add(IS_NEUTRAL, this.getType().equals(LITTLEGUY_ENTITY));
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.dataTracker.get(COLOR));
    }

    public void setColor(DyeColor color) {
        byte b = this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte) color.getId());
    }

    public boolean isNeutral() {
        return this.dataTracker.get(IS_NEUTRAL);
    }

    private void setIsNeutral(Boolean isNeutral) {
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

        this.dropItem(drop);
    }

    public boolean canLittleGuyAttackTarget(@Nullable LivingEntity target){
        if ((target instanceof LittleGuyEntity lgTarget) && !this.isNeutral()) {
            return !lgTarget.isNeutral() &&  (lgTarget.getColor() != this.getColor());
        }
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Color", (byte) this.getColor().getId());
        nbt.putBoolean("isNeutral", this.isNeutral());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setColor(DyeColor.byId(nbt.getByte("Color")));
        this.setIsNeutral(nbt.getBoolean("isNeutral"));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setColor(COLOR_LITTLEGUY_BIMAP.inverse().getOrDefault(this.getType(), DyeColor.WHITE));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    static {
        COLOR = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BYTE);
        COLORS = Arrays.stream(DyeColor.values())
                .collect(Collectors.toMap((color) -> color, LittleGuyEntity::getDyedColor));

        IS_NEUTRAL = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
