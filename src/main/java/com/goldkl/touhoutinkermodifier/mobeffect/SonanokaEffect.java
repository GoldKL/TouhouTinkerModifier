package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.spells.ice.ArcticstormSpell;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SonanokaEffect extends MobEffect {
    public SonanokaEffect() {
        super(MobEffectCategory.BENEFICIAL,0xa8265b);
        this.addAttributeModifier(AttributeRegistry.ENDER_SPELL_POWER.get(), "9b6aa38f-4d21-41df-bfe3-019c221e8032", 0.25, AttributeModifier.Operation.MULTIPLY_BASE);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(entity.level().isClientSide)return;
        if(entity.hasEffect(MobEffects.BLINDNESS))
        {
            entity.removeEffect(MobEffects.BLINDNESS);
        }
        if(entity.hasEffect(MobEffects.DARKNESS))
        {
            entity.removeEffect(MobEffects.DARKNESS);
        }
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
