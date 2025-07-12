package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.communication.FireworkMessage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = TouhouTinkerModifier.MODID)
public class ChannelEventTracker {
    @SubscribeEvent
    public static void setUp(FMLCommonSetupEvent event) {
        TouhouTinkerModifier.channel.messageBuilder(FireworkMessage.class, 0)
                .encoder(FireworkMessage::write)
                .decoder(FireworkMessage::new)
                .consumerMainThread(FireworkMessage::handle)
                .add();
    }
}
