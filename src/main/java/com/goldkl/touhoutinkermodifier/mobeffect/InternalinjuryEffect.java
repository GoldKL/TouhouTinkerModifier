package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class InternalinjuryEffect extends MobEffect {
    public InternalinjuryEffect() {
        super(MobEffectCategory.HARMFUL,0x8b0000);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "cbcaff7b-5968-4c4e-853f-77aac24b57ff", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "cbcaff7b-5968-4c4e-853f-77aac24b57ff", -4.0, AttributeModifier.Operation.ADDITION);
    }

    @SubscribeEvent
    static void livingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(MobeffectRegistry.INTERNALINJURY.get());
        if(effectInstance != null)
        {
            float level =  0.1f * effectInstance.getAmplifier() + 1.1f;
            event.setAmount(event.getAmount() * level);
        }
    }
    @Override
    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount()* Math.min(p_19457_ + 1 , 3);
    }
}
