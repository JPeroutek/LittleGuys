package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LittleHorseRenderer extends AbstractHorseEntityRenderer<LittleHorseEntity, LittleHorseEntityRenderState, LittleHorseModel<LittleHorseEntityRenderState>> {
    public LittleHorseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, createModel(ctx), createModel(ctx), .25f);
    }

    @Override
    public LittleHorseEntityRenderState createRenderState() {
        return new LittleHorseEntityRenderState();
    }

    private static LittleHorseModel<LittleHorseEntityRenderState> createModel(EntityRendererFactory.Context ctx) {
        EntityModelLayer entityModelLayer = ModModelLayers.LITTLEHORSE;
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return new LittleHorseModel<>(modelPart);
    }

    @Override
    protected void scale(LittleHorseEntityRenderState livingHorseEntityRenderState, MatrixStack matrixStack) {
        matrixStack.scale(.3f,.3f,.3f);
    }

    @Override
    public Identifier getTexture(LittleHorseEntityRenderState state) {
        return Identifier.ofVanilla("textures/block/clay.png");
    }
}
