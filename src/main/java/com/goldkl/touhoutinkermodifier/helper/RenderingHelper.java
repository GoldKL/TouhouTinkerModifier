package com.goldkl.touhoutinkermodifier.helper;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.bullettype.marisa.MarisaBBulletType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;

@OnlyIn(Dist.CLIENT)
public class RenderingHelper {
    public static final ResourceLocation KEY_NEED_TO_RENDER = TouhouTinkerModifier.getResource("key_need_to_render");

    public static void renderHelper(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            if(data.getBoolean(MarisaBBulletType.Key_Marisa_Laser))
            {
                /*if(!entity.isCrouching())
                {
                    Vec3 fix_position1 = RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() - 90, 1.5);
                    Vec3 fix_position2 = RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + 90, 1.5);
                    renderMarisaLaser(entity, poseStack, bufferSource, partialTicks,fix_position1);
                    renderMarisaLaser(entity, poseStack, bufferSource, partialTicks,fix_position2);
                }
                else
                {
                    Vec3 fix_position1 = RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 1.5);
                    renderMarisaLaser(entity, poseStack, bufferSource, partialTicks,fix_position1);
                }*/
                Vec3 fix_position1 = RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 1.5);
                renderMarisaLaser(entity, poseStack, bufferSource, partialTicks,fix_position1);
            }
        });
    }
    public static void renderMarisaLaser(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks,Vec3 fixpostion) {
        poseStack.pushPose();
        poseStack.translate(0, entity.getEyeHeight() * .8f, 0);
        poseStack.translate(fixpostion.x(), fixpostion.y(), fixpostion.z());
        var pose = poseStack.last();
        Vec3 start = Vec3.ZERO;
        Vec3 end;
        float distance = 40;
        float radius = .12f;
        int r = 85;
        int g = 85;
        int b = 255;
        int a = 255;
        float deltaTicks = entity.tickCount + partialTicks;
        float deltaUV = -deltaTicks % 10;
        float max = Mth.frac(deltaUV * 0.2F - (float) Mth.floor(deltaUV * 0.1F));
        float min = -1.0F + max;
        var dir = entity.getLookAngle().normalize();
        float dx = (float) dir.x;
        float dz = (float) dir.z;
        float yRot = (float) Mth.atan2(dz, dx) - 1.5707f;
        float dxz = Mth.sqrt(dx * dx + dz * dz);
        float dy = (float) dir.y;
        float xRot = (float) Mth.atan2(dy, dxz);
        poseStack.mulPose(Axis.YP.rotation(-yRot));
        poseStack.mulPose(Axis.XP.rotation(-xRot));
        for (float j = 1; j <= distance; j += .5f) {
            end = new Vec3(0, 0, Math.min(j, distance));
            VertexConsumer inner = bufferSource.getBuffer(RenderType.entityTranslucent(SpellRenderingHelper.STRAIGHT_GLOW, true));
            drawHull(start, end, radius, radius, pose, inner, r, g, b, a, min, max);
            VertexConsumer outer = bufferSource.getBuffer(RenderType.entityTranslucent(SpellRenderingHelper.STRAIGHT_GLOW));
            drawQuad(start, end, radius * 4f, 0, pose, outer, r, g, b, a, min, max);
            drawQuad(start, end, 0, radius * 4f, pose, outer, r, g, b, a, min, max);
            start = end;
        }
        poseStack.popPose();
    }

    private static void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        //Bottom
        drawQuad(from.subtract(0, height * .5f, 0), to.subtract(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Top
        drawQuad(from.add(0, height * .5f, 0), to.add(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Left
        drawQuad(from.subtract(width * .5f, 0, 0), to.subtract(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Right
        drawQuad(from.add(width * .5f, 0, 0), to.add(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
    }

    private static void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        float halfWidth = width * .5f;
        float halfHeight = height * .5f;

        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();

    }
}
