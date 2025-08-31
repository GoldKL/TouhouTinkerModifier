package com.goldkl.touhoutinkermodifier.communication;

import com.goldkl.touhoutinkermodifier.capability.TheKindofKillDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TheKindofKillDataMessage {
    private CompoundTag capabilityNBT;

    public TheKindofKillDataMessage(TheKindofKillDataCapability.LivingEntityList killList) {
        var tag = new CompoundTag();
        killList.saveNBTData(tag);
        this.capabilityNBT = tag;
    }

    public TheKindofKillDataMessage(FriendlyByteBuf buffer) {
        this.capabilityNBT = buffer.readNbt();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(capabilityNBT);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(this, context));
    }

    private static class Handler {
        static void handle(TheKindofKillDataMessage message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                Player player = Minecraft.getInstance().player;
                assert player != null;

                player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(list -> {
                    list.loadNBTData(message.capabilityNBT);
                });
            });
            context.get().setPacketHandled(true);
        }
    }
}
