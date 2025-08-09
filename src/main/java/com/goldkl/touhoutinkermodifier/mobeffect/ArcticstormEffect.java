package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.spells.ice.ArcticstormSpell;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArcticstormEffect extends MobEffect {
    public ArcticstormEffect() {
        super(MobEffectCategory.BENEFICIAL,0xB9CDF6);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(entity.level().isClientSide)return;
        if(amplifier < 0) amplifier = amplifier & 0xFF;
        ArcticstormSpell spell = (ArcticstormSpell) SpellsRegistry.arcticstorm.get();
        spell.applyDamage(amplifier + 1 , entity);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
