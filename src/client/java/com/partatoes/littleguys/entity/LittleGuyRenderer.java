package com.partatoes.littleguys.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;


public class LittleGuyRenderer extends HumanoidMobRenderer<LittleGuyEntity, LittleGuyEntityRenderState, LittleGuyModel<LittleGuyEntityRenderState>> {
    public LittleGuyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, createModel(ctx), .25f);
    }

    @Override
    public LittleGuyEntityRenderState createRenderState() {
        return new LittleGuyEntityRenderState();
    }
    @Override
    public void extractRenderState(LittleGuyEntity entity, LittleGuyEntityRenderState renderState, float f) {
        super.extractRenderState(entity, renderState, f);
        renderState.color = entity.getColor();
        renderState.isNeutral = entity.isNeutral();
    }

    private static LittleGuyModel<LittleGuyEntityRenderState> createModel(EntityRendererProvider.Context ctx) {
        ModelLayerLocation entityModelLayer = ModModelLayers.LITTLEGUY;
        ModelPart modelPart = ctx.bakeLayer(entityModelLayer);
        return new LittleGuyModel<>(modelPart);
    }

    @Override
    protected void scale(LittleGuyEntityRenderState state, PoseStack matrices) {
        matrices.scale(.3f,.3f,.3f);
    }

    @Override
    protected int getModelTint(LittleGuyEntityRenderState state) {
        if (state.isNeutral) {
            // Eventually, just pick a color, rather than this mixing business.  Two parts white, one part light gray.
            return ARGB.multiply(ARGB.multiply(DyeColor.LIGHT_GRAY.getTextureDiffuseColor(), DyeColor.WHITE.getTextureDiffuseColor()), DyeColor.WHITE.getTextureDiffuseColor());
        } else {
            return state.color.getTextureDiffuseColor();
        }
    }

    @Override
    public Identifier getTextureLocation(LittleGuyEntityRenderState state) {
        return Identifier.withDefaultNamespace("textures/block/white_concrete_powder.png");
    }
}
