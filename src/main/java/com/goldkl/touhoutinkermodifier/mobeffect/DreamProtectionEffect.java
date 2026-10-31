package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class DreamProtectionEffect extends MobEffect {
    public DreamProtectionEffect() {
        super(MobEffectCategory.BENEFICIAL,0xce9fff);
    }
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(10 * (amplifier + 1)));
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(10 * (amplifier + 1)));
        super.addAttributeModifiers(entity, attributeMap, amplifier);
    }
}
