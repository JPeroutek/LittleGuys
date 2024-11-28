package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class LittleGuyRenderer extends BipedEntityRenderer<LittleGuyEntity, LittleGuyEntityRenderState, LittleGuyModel<LittleGuyEntityRenderState>> {
    public LittleGuyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, createModel(ctx), .25f);
    }

    @Override
    public LittleGuyEntityRenderState createRenderState() {
        return null;
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
    public void render(LittleGuyEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

//    @Override
//    public void renderOld(LittleGuyEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
//        // Most of this is taken from the LivingEntityRenderer class, with some additions to get the right color
//        float n;
//        Direction direction;
//        Entity entity;
//        matrixStack.push();
//        this.model.handSwingProgress = this.getHandSwingProgress(mobEntity, g);
//        this.model.riding = mobEntity.hasVehicle();
//        this.model.child = mobEntity.isBaby();
//        float h = MathHelper.lerpAngleDegrees((float)g, (float)((LivingEntity)mobEntity).prevBodyYaw, (float)((LivingEntity)mobEntity).bodyYaw);
//        float j = MathHelper.lerpAngleDegrees((float)g, (float)((LivingEntity)mobEntity).prevHeadYaw, (float)((LivingEntity)mobEntity).headYaw);
//        float k = j - h;
//        if (mobEntity.hasVehicle() && (entity = mobEntity.getVehicle()) instanceof LivingEntity) {
//            LivingEntity mobEntity2 = (LivingEntity)entity;
//            h = MathHelper.lerpAngleDegrees((float)g, (float)mobEntity2.prevBodyYaw, (float)mobEntity2.bodyYaw);
//            k = j - h;
//            float l = MathHelper.wrapDegrees((float)k);
//            if (l < -85.0f) {
//                l = -85.0f;
//            }
//            if (l >= 85.0f) {
//                l = 85.0f;
//            }
//            h = j - l;
//            if (l * l > 2500.0f) {
//                h += l * 0.2f;
//            }
//            k = j - h;
//        }
//        float m = MathHelper.lerp((float)g, (float)((LivingEntity)mobEntity).prevPitch, (float)mobEntity.getPitch());
//        if (LivingEntityRenderer.shouldFlipUpsideDown(mobEntity)) {
//            m *= -1.0f;
//            k *= -1.0f;
//        }
//        k = MathHelper.wrapDegrees((float)k);
//        if (mobEntity.isInPose(EntityPose.SLEEPING) && (direction = mobEntity.getSleepingDirection()) != null) {
//            n = mobEntity.getEyeHeight(EntityPose.STANDING) - 0.1f;
//            matrixStack.translate((float)(-direction.getOffsetX()) * n, 0.0f, (float)(-direction.getOffsetZ()) * n);
//        }
//        float l = mobEntity.getScale();
//        matrixStack.scale(l, l, l);
//        n = this.getAnimationProgress(mobEntity, g);
//        this.setupTransforms(mobEntity, matrixStack, n, h, g, l);
//        matrixStack.scale(-1.0f, -1.0f, 1.0f);
//        this.scale(mobEntity, matrixStack, g);
//        matrixStack.translate(0.0f, -1.501f, 0.0f);
//        float o = 0.0f;
//        float p = 0.0f;
//        if (!mobEntity.hasVehicle() && mobEntity.isAlive()) {
//            o = ((LivingEntity)mobEntity).limbAnimator.getSpeed(g);
//            p = ((LivingEntity)mobEntity).limbAnimator.getPos(g);
//            if (mobEntity.isBaby()) {
//                p *= 3.0f;
//            }
//            if (o > 1.0f) {
//                o = 1.0f;
//            }
//        }
//        ((EntityModel)this.model).animateModel(mobEntity, p, o, g);
//        ((EntityModel)this.model).setAngles(mobEntity, p, o, n, k, m);
//        MinecraftClient minecraftClient = MinecraftClient.getInstance();
//        boolean bl = this.isVisible(mobEntity);
//        boolean bl2 = !bl && !mobEntity.isInvisibleTo((PlayerEntity)minecraftClient.player);
//        boolean bl3 = minecraftClient.hasOutline((Entity)mobEntity);
//        RenderLayer renderLayer = this.getRenderLayer(mobEntity, bl, bl2, bl3);
//        if (renderLayer != null) {
//            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
//            int q = LivingEntityRenderer.getOverlay(mobEntity, this.getAnimationCounter(mobEntity, g));
//
//            int renderColor = mobEntity.getColor().getEntityColor();
//
//            if (mobEntity.isNeutral()) {
//                ((Model) this.model).render(matrixStack, vertexConsumer, i, q);
//            } else {
//                ((Model) this.model).render(matrixStack, vertexConsumer, i, q, renderColor);
//            }
//        }
//        matrixStack.pop();
//    }

    @Override
    public Identifier getTexture(LittleGuyEntityRenderState state) {
        return Identifier.ofVanilla("textures/block/clay.png");
    }
}
