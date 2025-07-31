package com.goldkl.touhoutinkermodifier.communication.spells.ice;

import com.goldkl.touhoutinkermodifier.communication.FireworkMessage;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientAbsolutezeroParticles {
    private final Vec3 pos;
    private final float radius;
    public ClientAbsolutezeroParticles(Vec3 pos,float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public ClientAbsolutezeroParticles(FriendlyByteBuf buf) {
        pos = readVec3(buf);
        radius = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        writeVec3(pos, buf);
        buf.writeFloat(radius);
    }

    public Vec3 readVec3(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }

    public void writeVec3(Vec3 vec3, FriendlyByteBuf buf) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(this, supplier));
        supplier.get().setPacketHandled(true);
    }
    private static class Handler {
        static void handle(ClientAbsolutezeroParticles message, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    Vec3 pos = message.pos;
                    float radius = message.radius;
                    var level = player.level();
                    int ySteps = 64;
                    float yDeg = 360f / ySteps * Mth.DEG_TO_RAD;
                    for (int h = -4; h <= 4; h++) {
                        for (int y = 0; y < ySteps; y++) {
                            Vec3 offset = new Vec3(0, h, radius).yRot(y * yDeg);
                            level.addParticle(ParticleHelper.SNOWFLAKE, pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, 0, 0.05, 0);
                        }
                    }
                }
            });
        }
    }
}
