package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.communication.FireworkMessage;
import com.goldkl.touhoutinkermodifier.communication.spells.ice.ClientAbsolutezeroParticles;
import com.goldkl.touhoutinkermodifier.communication.spells.ice.ClientArcticstormParticles;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = TouhouTinkerModifier.MODID)
public class ChannelEventTracker {
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }
    @SubscribeEvent
    public static void setUp(FMLCommonSetupEvent event) {
        TouhouTinkerModifier.channel.messageBuilder(FireworkMessage.class, id())
                .encoder(FireworkMessage::write)
                .decoder(FireworkMessage::new)
                .consumerMainThread(FireworkMessage::handle)
                .add();
        TouhouTinkerModifier.channel.messageBuilder(ClientAbsolutezeroParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientAbsolutezeroParticles::toBytes)
                .decoder(ClientAbsolutezeroParticles::new)
                .consumerMainThread(ClientAbsolutezeroParticles::handle)
                .add();
        TouhouTinkerModifier.channel.messageBuilder(ClientArcticstormParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientArcticstormParticles::toBytes)
                .decoder(ClientArcticstormParticles::new)
                .consumerMainThread(ClientArcticstormParticles::handle)
                .add();
    }
    public static <MSG> void sendToServer(MSG message) {
        TouhouTinkerModifier.channel.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        TouhouTinkerModifier.channel.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        TouhouTinkerModifier.channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        TouhouTinkerModifier.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer)
            sendToPlayer(message, serverPlayer);
    }
}
