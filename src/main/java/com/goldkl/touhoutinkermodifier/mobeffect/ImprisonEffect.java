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

public class ImprisonEffect extends MobEffect {
    public ImprisonEffect() {
        super(MobEffectCategory.HARMFUL,0x0000FF);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "72173e45-3d01-4095-92bf-62eb25d1efcb", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.FLYING_SPEED, "72173e45-3d01-4095-92bf-62eb25d1efcb", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    @Override
    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount();
    }
}
