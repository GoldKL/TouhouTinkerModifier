package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class DeathfireEffect extends MobEffect {
    public DeathfireEffect() {
        super(MobEffectCategory.HARMFUL,0x515dff);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void livingDamage(LivingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if(!(attacker instanceof LivingEntity lvattack) || event.getSource().isIndirect()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(MobeffectRegistry.DEATHFIRE.get());
        if(effectInstance != null)
        {
            float percent = (effectInstance.getAmplifier() + 1) * 0.03f;
            lvattack.heal(event.getAmount() * percent);
        }
    }
}
