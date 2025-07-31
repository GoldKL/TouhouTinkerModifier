package com.goldkl.touhoutinkermodifier.mobeffect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SonanokaEffect extends MobEffect {
    public SonanokaEffect() {
        super(MobEffectCategory.BENEFICIAL,0xa8265b);
        this.addAttributeModifier(AttributeRegistry.ENDER_SPELL_POWER.get(), "9b6aa38f-4d21-41df-bfe3-019c221e8032", 0.25, AttributeModifier.Operation.MULTIPLY_BASE);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
