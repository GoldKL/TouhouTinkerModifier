package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BreakdarknessEffect extends MobEffect {
    public BreakdarknessEffect() {
        super(MobEffectCategory.HARMFUL,0x610000);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
