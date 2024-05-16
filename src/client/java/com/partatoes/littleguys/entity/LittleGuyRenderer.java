package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LittleGuyRenderer extends BipedEntityRenderer<LittleGuyEntity, LittleGuyModel<LittleGuyEntity>> {
    public LittleGuyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, createModel(ctx), .25f);
    }

    private static LittleGuyModel<LittleGuyEntity> createModel(EntityRendererFactory.Context ctx) {
        EntityModelLayer entityModelLayer = ModModelLayers.LITTLEGUY;
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return new LittleGuyModel<>(modelPart);
    }

    @Override
    public Identifier getTexture(LittleGuyEntity entity) {
        return new Identifier("textures/block/clay.png");
    }

    @Override
    protected void scale(LittleGuyEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(.3f,.3f,.3f);
    }
}
