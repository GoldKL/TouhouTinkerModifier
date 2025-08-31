package com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;

@Mixin(EffectEventHandlers.class)
public class EffectEventHandlersMixin {
    @Inject(method = "removeKoishi",at =@At("HEAD"),remap = false)
    private static void removeKoishimixin(LivingEntity le, CallbackInfo ci)
    {
        if(le.hasEffect(YHEffects.UNCONSCIOUS.get()))
        {
            le.removeEffect(YHEffects.UNCONSCIOUS.get());
            le.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                data.putInt(ModifierIds.koishiseye, 200);
                if(le instanceof Player player && !le.level().isClientSide)
                {
                    TinkerNetwork.getInstance().sendTo(new SyncPersistentDataPacket(data.getCopy()), player);
                }
            });

        }
    }
    @Inject(method = "disableKoishi",at = @At("TAIL"),remap = false,locals = LocalCapture.CAPTURE_FAILHARD)
    private static void disableKoishimixin(Player player, CallbackInfo ci, boolean flag) {
        if(flag)
        {
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                data.putInt(ModifierIds.koishiseye, 200);
                if(!player.level().isClientSide)
                {
                    TinkerNetwork.getInstance().sendTo(new SyncPersistentDataPacket(data.getCopy()), player);
                }
            });
        }
    }
    @Inject(method = "onAttack",at = @At("TAIL"),remap = false)
    private static void onAttackmixin(LivingAttackEvent event, CallbackInfo ci) {
        if (event.getSource().getEntity() instanceof LivingEntity le) {
            if (!(le instanceof Player)) {
                touhouTinkerModifier$disableKoishi(le);
            }
        }
    }
    @Unique
    private static void touhouTinkerModifier$disableKoishi(LivingEntity entity) {
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            boolean flag = false;
            if (entity.hasEffect(YHEffects.UNCONSCIOUS.get())) {
                entity.removeEffect(YHEffects.UNCONSCIOUS.get());
                flag = true;
            }
            int time = data.getInt(ModifierIds.koishiseye);
            if(time > 0)
            {
                flag = true;
            }
            if(flag)
            {
                data.putInt(ModifierIds.koishiseye, 200);
            }
        });
    }
}
