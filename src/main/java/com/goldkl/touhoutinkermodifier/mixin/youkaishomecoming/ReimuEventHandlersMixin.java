package com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.youkaishomecoming.events.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;

@Mixin(ReimuEventHandlers.class)
public class ReimuEventHandlersMixin {
    @Inject(method = "koishiBlockReimu",at = @At("HEAD"),remap = false, cancellable = true)
    private static void koishiBlockReimuMixin(LivingEntity le, CallbackInfoReturnable<Boolean> cir)
    {
        if(le instanceof ServerPlayer sp
                && TTMEntityUtils.hasModifier(sp, ModifierIds.koishiseye, ModifiableArmorMaterial.ARMOR_SLOTS))
        {
            sp.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data->{
                int time = data.getInt(ModifierIds.koishiseye);
                if(time == 0)
                {
                    if (sp.hasEffect(YHEffects.UNCONSCIOUS.get())) {
                        sp.removeEffect(YHEffects.UNCONSCIOUS.get());
                    }
                    data.putInt(ModifierIds.koishiseye, 1200);
                    TinkerNetwork.getInstance().sendTo(new SyncPersistentDataPacket(data.getCopy()), sp);
                    sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
                    sp.sendSystemMessage(YHLangData.KOISHI_REIMU.get(), true);
                    cir.setReturnValue(true);
                }
            });
        }
    }
}
