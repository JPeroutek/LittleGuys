package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LittleHorseRenderer extends AbstractHorseEntityRenderer<LittleHorseEntity, LittleHorseModel<LittleHorseEntity>> {
    public LittleHorseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, createModel(ctx), .25f);
    }

    private static LittleHorseModel<LittleHorseEntity> createModel(EntityRendererFactory.Context ctx) {
        EntityModelLayer entityModelLayer = ModModelLayers.LITTLEHORSE;
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return new LittleHorseModel<>(modelPart);
    }

    @Override
    public Identifier getTexture(LittleHorseEntity entity) {
        return new Identifier("textures/block/clay.png");
    }

    @Override
    protected void scale(LittleHorseEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(.3f,.3f,.3f);
    }
}
