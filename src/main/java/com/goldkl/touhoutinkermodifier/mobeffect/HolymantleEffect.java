package com.goldkl.touhoutinkermodifier.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class HolymantleEffect extends MobEffect {
    public HolymantleEffect() {
        super(MobEffectCategory.BENEFICIAL,0x91D4E2);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
