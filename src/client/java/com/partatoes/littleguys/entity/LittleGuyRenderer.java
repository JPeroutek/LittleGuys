package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;


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
    public void render(LittleGuyEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        // Most of this is taken from the LivingEntityRenderer.render method, with some additions to get the right color.
        //  If you are updating this mod to a newer version, try to figure out how to not duplicate so much code here...
        matrixStack.push();
        if (state.isInPose(EntityPose.SLEEPING)) {
            Direction direction = state.sleepingDirection;
            if (direction != null) {
                float f = state.standingEyeHeight - 0.1F;
                matrixStack.translate((float)(-direction.getOffsetX()) * f, 0.0F, (float)(-direction.getOffsetZ()) * f);
            }
        }

        float g = state.baseScale;
        matrixStack.scale(g, g, g);
        this.setupTransforms(state, matrixStack, state.bodyYaw, g);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(state, matrixStack);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        this.model.setAngles(state);
        boolean bl = this.isVisible(state);
        boolean bl2 = !bl && !state.invisibleToPlayer;
        RenderLayer renderLayer = this.getRenderLayer(state, bl, bl2, state.hasOutline);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int j = getOverlay(state, this.getAnimationCounter(state));
            this.model.render(matrixStack, vertexConsumer, i, j, state.color.getEntityColor());
        }

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(LittleGuyEntityRenderState state) {
        return Identifier.ofVanilla("textures/block/clay.png");
    }
}
