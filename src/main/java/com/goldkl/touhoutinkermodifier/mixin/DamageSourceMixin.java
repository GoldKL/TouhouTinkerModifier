package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.api.DamageModifierDamageSource;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements DamageModifierDamageSource {
    @Unique
    private DamageModifier touhouTinkerModifier$damageModifier = null;

    @Unique
    @Override
    public DamageModifier touhouTinkerModifier$getDamageModifier() {
        return touhouTinkerModifier$damageModifier;
    }

    @Unique
    @Override
    public void touhouTinkerModifier$setDamageModifier(DamageModifier damage) {
        touhouTinkerModifier$damageModifier = new DamageModifier(damage);
    }
}
