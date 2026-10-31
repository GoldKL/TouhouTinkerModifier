package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class DeathfireEffect extends MobEffect {

    public DeathfireEffect() {
        super(MobEffectCategory.HARMFUL,0x515dff);
        this.addAttributeModifier(AttributeRegistry.FIRE_MAGIC_RESIST.get(), "4e8a4b4d-ad3b-4a74-be14-0fd01b83c3de", 0.8, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }
    @Override
    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        if(p_19457_ < 0) p_19457_ = p_19457_ & 0xFF;
        return Math.pow(p_19458_.getAmount(),p_19457_ + 1) - 1;
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
