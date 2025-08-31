package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExhaustedEffect extends MobEffect {
    public ExhaustedEffect() {
        super(MobEffectCategory.HARMFUL,0x808080);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "fdf0aadb-46be-4a3a-8b88-8b7b1a235da1", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "fdf0aadb-46be-4a3a-8b88-8b7b1a235da1", -4, AttributeModifier.Operation.ADDITION);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
