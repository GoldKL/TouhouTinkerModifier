package com.goldkl.touhoutinkermodifier.mobeffect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class MeltEffect extends MobEffect {
    public MeltEffect() {
        super(MobEffectCategory.HARMFUL,0xE6E6FA);
        this.addAttributeModifier(AttributeRegistry.SPELL_RESIST.get(), "5486256b-57ec-4362-8b6c-c7269fc1dcc0", 0.88, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    @Override
    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        if(p_19457_ < 0) p_19457_ = p_19457_ & 0xFF;
        return Math.pow(p_19458_.getAmount(),p_19457_ + 1) - 1;
    }
}
