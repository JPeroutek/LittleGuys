package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;


public class LittleGuyRenderer extends BipedEntityRenderer<LittleGuyEntity, LittleGuyEntityRenderState, LittleGuyModel<LittleGuyEntityRenderState>> {
    public LittleGuyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, createModel(ctx), .25f);
    }

    @Override
    public LittleGuyEntityRenderState createRenderState() {
        return new LittleGuyEntityRenderState();
    }
    @Override
    public void updateRenderState(LittleGuyEntity entity, LittleGuyEntityRenderState renderState, float f) {
        super.updateRenderState(entity, renderState, f);
        renderState.color = entity.getColor();
        renderState.isNeutral = entity.isNeutral();
    }

    private static LittleGuyModel<LittleGuyEntityRenderState> createModel(EntityRendererFactory.Context ctx) {
        EntityModelLayer entityModelLayer = ModModelLayers.LITTLEGUY;
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return new LittleGuyModel<>(modelPart);
    }

    @Override
    protected void scale(LittleGuyEntityRenderState state, MatrixStack matrices) {
        matrices.scale(.3f,.3f,.3f);
    }

    @Override
    protected int getMixColor(LittleGuyEntityRenderState state) {
        if (state.isNeutral) {
            // Eventually, just pick a color, rather than this mixing business.  Two parts white, one part light gray.
            return ColorHelper.mix(ColorHelper.mix(DyeColor.LIGHT_GRAY.getEntityColor(), DyeColor.WHITE.getEntityColor()), DyeColor.WHITE.getEntityColor());
        } else {
            return state.color.getEntityColor();
        }
    }

    @Override
    public Identifier getTexture(LittleGuyEntityRenderState state) {
        return Identifier.ofVanilla("textures/block/white_concrete_powder.png");
    }
}
