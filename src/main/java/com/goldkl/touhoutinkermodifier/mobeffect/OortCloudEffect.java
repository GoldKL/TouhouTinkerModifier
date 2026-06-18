package com.goldkl.touhoutinkermodifier.mobeffect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class OortCloudEffect extends MobEffect {
    public OortCloudEffect() {
        super(MobEffectCategory.BENEFICIAL,0xFFFF66);
        this.addAttributeModifier(Attributes.ARMOR, "671f458f-a2e4-4ac8-a0ee-a2f701aac81c", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(AttributeRegistry.SPELL_RESIST.get(), "3de02057-18b4-4969-8e0c-9c87c7cd7a05", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);

    }
}
