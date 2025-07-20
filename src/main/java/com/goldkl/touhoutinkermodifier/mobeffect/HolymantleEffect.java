package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HolymantleEffect extends MobEffect {
    public HolymantleEffect() {
        super(MobEffectCategory.BENEFICIAL,0x91D4E2);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
