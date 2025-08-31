package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FinedrunkEffect extends MobEffect {
    public FinedrunkEffect() {
        super(MobEffectCategory.BENEFICIAL,0x061932);
        String uuid = "9cc04768-015e-44ed-9df1-79da4e5b4edb";
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 2.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, uuid, 2.0, AttributeModifier.Operation.ADDITION);
    }
}
