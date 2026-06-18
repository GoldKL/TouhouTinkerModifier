package com.goldkl.touhoutinkermodifier.mobeffect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class OrrerysSolarSystemEffect extends MobEffect {
    public OrrerysSolarSystemEffect() {
        super(MobEffectCategory.BENEFICIAL,0xfbac13);
        this.addAttributeModifier(AttributeRegistry.FIRE_SPELL_POWER.get(), "68088736-54b0-419e-a055-9dc0fa945d5b", 0.05, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
