package com.partatoes.littleguys.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LittleHorseEntity extends AbstractHorseEntity {

    public LittleHorseEntity(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createLittleHorseAttributes() {
        return createBaseHorseAttributes()
                .add(EntityAttributes.MAX_HEALTH, 6);
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return Entity.getPassengerAttachmentPos(this, passenger, dimensions.attachments()).add(new Vec3d(0.0, (0.15 * (double)this.lastAngryAnimationProgress * (double)scaleFactor) - 0.15, -0.15 * (double)this.lastAngryAnimationProgress * (double)scaleFactor).rotateY(-this.getYaw() * ((float)Math.PI / 180)));
    }
}
