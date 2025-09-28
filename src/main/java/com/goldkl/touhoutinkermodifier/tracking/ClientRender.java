package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.helper.RenderingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID, value = Dist.CLIENT)
public class ClientRender {
    @SubscribeEvent
    public static void afterLivingRender(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        var livingEntity = event.getEntity();
        livingEntity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int num = Math.max(0,data.getInt(RenderingHelper.KEY_NEED_TO_RENDER));
            if(num > 0)
            {
                RenderingHelper.renderHelper(livingEntity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }
        });
    }

}
