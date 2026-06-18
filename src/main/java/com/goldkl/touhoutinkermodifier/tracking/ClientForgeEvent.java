package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID,value = Dist.CLIENT)
public class ClientForgeEvent {
    private static final float MAX_DELTA_DEGREES_LOW = 5.0f;
    private static final float MAX_DELTA_DEGREES_HIGH = 20.0f;

    @SubscribeEvent
    public static void MasterSparkEvent(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        MouseHandler mouse = mc.mouseHandler;

        if (player == null) return;
        boolean flag = false;
        if(ClientMagicData.isCasting()){
            if(ClientMagicData.getCastingSpellId().equals("touhoutinkermodifier:master_spark")){
                flag = true;
            }
        }
        if(!flag)return;
        double rawDx = mouse.getXVelocity();
        double rawDy = mouse.getYVelocity();

        if (rawDx == 0 && rawDy == 0) return;
        float MAX_DELTA_DEGREES = ClientMagicData.getCastingSpellLevel() < 9 ? MAX_DELTA_DEGREES_LOW:MAX_DELTA_DEGREES_HIGH;
        float sensitivity = mc.options.sensitivity().get().floatValue();
        float deltaYaw = (float) (rawDx * 0.15f * sensitivity);
        float deltaPitch = (float) (rawDy * 0.15f * sensitivity * -1.0f); // 注意Pitch方向相反

        deltaYaw = Mth.clamp(deltaYaw, -MAX_DELTA_DEGREES, MAX_DELTA_DEGREES);
        deltaPitch = Mth.clamp(deltaPitch, -MAX_DELTA_DEGREES, MAX_DELTA_DEGREES);

        player.turn(deltaYaw, deltaPitch);
    }
}
