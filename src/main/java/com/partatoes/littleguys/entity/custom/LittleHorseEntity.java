package com.partatoes.littleguys.entity.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LittleHorseEntity extends AbstractHorse {

    public LittleHorseEntity(EntityType<? extends AbstractHorse> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createLittleHorseAttributes() {
        return createBaseHorseAttributes()
                .add(Attributes.MAX_HEALTH, 6);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return Entity.getDefaultPassengerAttachmentPoint(this, passenger, dimensions.attachments()).add(new Vec3(0.0, (0.15 * (double)this.standAnimO * (double)scaleFactor) - 0.15, -0.15 * (double)this.standAnimO * (double)scaleFactor).yRot(-this.getYRot() * ((float)Math.PI / 180)));
    }
}
