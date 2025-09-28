package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuRenderer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.common.ClientEventBase;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;

import java.util.Set;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientSetup extends ClientEventBase {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntitiesRegistry.SkySplitter.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.TrackDanmaku.get(), ItemDanmakuRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.ModifiableDanmaku.get(), ItemDanmakuRenderer::new);
    }
    static public final ResourceLocation ANIMA = TouhouTinkerModifier.getResource("animation");

    @SubscribeEvent
    static void clientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TinkerItemProperties.registerToolProperties(ItemsRegistry.Spear);
            TinkerItemProperties.registerToolProperties(ItemsRegistry.BrassKnuckles);
            TinkerItemProperties.registerToolProperties(ItemsRegistry.Gohei);
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ANIMA,
                42,
                player -> new ModifierLayer<>());
    }
    @SubscribeEvent
    static void itemColors(RegisterColorHandlersEvent.Item event) {
        final ItemColors colors = event.getItemColors();
        ToolModel.registerItemColors(colors, ItemsRegistry.Spear);
        ToolModel.registerItemColors(colors, ItemsRegistry.BrassKnuckles);
        ToolModel.registerItemColors(colors, ItemsRegistry.Gohei);
    }
}
