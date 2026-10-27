package com.partatoes.littleguys.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;

public class LittleHorseRenderer extends AbstractHorseRenderer<LittleHorseEntity, LittleHorseEntityRenderState, LittleHorseModel<LittleHorseEntityRenderState>> {
    public LittleHorseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, createModel(ctx), createModel(ctx));
    }

    @Override
    public LittleHorseEntityRenderState createRenderState() {
        return new LittleHorseEntityRenderState();
    }

    private static LittleHorseModel<LittleHorseEntityRenderState> createModel(EntityRendererProvider.Context ctx) {
        ModelLayerLocation entityModelLayer = ModModelLayers.LITTLEHORSE;
        ModelPart modelPart = ctx.bakeLayer(entityModelLayer);
        return new LittleHorseModel<>(modelPart);
    }

    @Override
    protected void scale(LittleHorseEntityRenderState livingHorseEntityRenderState, PoseStack matrixStack) {
        matrixStack.scale(.3f,.3f,.3f);
    }

    @Override
    public Identifier getTextureLocation(LittleHorseEntityRenderState state) {
        return Identifier.withDefaultNamespace("textures/block/white_concrete_powder.png");
    }

    @Override
    protected int getModelTint(LittleHorseEntityRenderState state) {
        return ARGB.multiply(ARGB.multiply(DyeColor.LIGHT_GRAY.getTextureDiffuseColor(), DyeColor.WHITE.getTextureDiffuseColor()), DyeColor.WHITE.getTextureDiffuseColor());
    }
}
