package com.partatoes.littleguys.entity.custom;

import com.partatoes.littleguys.entity.goal.BoardLittleHorseGoal;
import com.partatoes.littleguys.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
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

public class LittleGuyEntity extends MobEntity {

    private static final TrackedData<Byte> COLOR;
    private static final Map<DyeColor, float[]> COLORS;

    public LittleGuyEntity(EntityType<? extends LittleGuyEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new BoardLittleHorseGoal(this));
    }

    public static DefaultAttributeContainer.Builder createLittleGuyAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .8);
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
        this.setColor(DyeColor.byId(16));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    static {
        COLOR = DataTracker.registerData(LittleGuyEntity.class, TrackedDataHandlerRegistry.BYTE);
        COLORS = Arrays.stream(DyeColor.values())
                .collect(Collectors.toMap((color) -> color, LittleGuyEntity::getDyedColor));
    }
}
