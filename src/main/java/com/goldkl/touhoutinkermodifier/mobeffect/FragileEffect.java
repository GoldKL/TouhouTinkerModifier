package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class FragileEffect extends MobEffect {
    public FragileEffect() {
        super(MobEffectCategory.HARMFUL,0xFF0000);
    }
    @SubscribeEvent
    static void livingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(MobeffectRegistry.FRAGILE.get());
        if(effectInstance != null)
        {
            float level =  0.2f * effectInstance.getAmplifier() + 1.2f;
            event.setAmount(event.getAmount() * level);
            entity.removeEffect(MobeffectRegistry.FRAGILE.get());
        }
    }
}
