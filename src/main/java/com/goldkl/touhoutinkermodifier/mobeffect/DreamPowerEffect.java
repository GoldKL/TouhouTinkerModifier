package com.goldkl.touhoutinkermodifier.mobeffect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class DreamPowerEffect extends MobEffect {
    public DreamPowerEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xff9ff5);
        this.addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), "1bbf60e3-4b4c-4826-9b43-00f88a0e2652", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}