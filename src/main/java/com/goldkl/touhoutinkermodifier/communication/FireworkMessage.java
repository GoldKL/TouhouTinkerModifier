package com.goldkl.touhoutinkermodifier.communication;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FireworkMessage {
    final private double x;
    final private double y;
    final private double z;
    final private double vx;
    final private double vy;
    final private double vz;
    final CompoundTag compoundtag;
    public FireworkMessage(double x, double y, double z, double vx, double vy, double vz,@Nullable CompoundTag compoundtag) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        if (compoundtag != null)
            this.compoundtag = compoundtag.copy();
        else
            this.compoundtag = null;
    }

    public FireworkMessage(FriendlyByteBuf buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.vx = buffer.readDouble();
        this.vy = buffer.readDouble();
        this.vz = buffer.readDouble();
        CompoundTag tem = buffer.readNbt();
        if (tem != null) {
            this.compoundtag = tem.copy();
        }
        else {
            this.compoundtag = null;
        }
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(vx);
        buffer.writeDouble(vy);
        buffer.writeDouble(vz);
        buffer.writeNbt(compoundtag);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        if(context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            context.get().enqueueWork(() -> TouhouTinkerModifier.channel.send(PacketDistributor.ALL.noArg(), this));
        }
        else {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(this, context));
        }
        context.get().setPacketHandled(true);
    }

    private static class Handler {
        static void handle(FireworkMessage message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                double x = message.x;
                double y = message.y;
                double z = message.z;
                double vx = message.vx;
                double vy = message.vy;
                double vz = message.vz;
                CompoundTag tem = message.compoundtag.copy();
                Minecraft.getInstance().level.createFireworks(x, y, z, vx, vy, vz, tem);
            });
        }
    }
}
