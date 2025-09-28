package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffect.class)
public class MobEffectMixin {
    @WrapOperation(method = "applyEffectTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    boolean applyEffectTickMixin(LivingEntity entity, DamageSource source, float ev, Operation<Boolean> original)
    {
        boolean flag = TTMEntityUtils.hasModifier(entity, TTMModifierIds.fieldmiasma);
        if(flag)
        {
            if(((MobEffect) (Object)this) == MobEffects.POISON)
            {
                return false;
            }
            else if(((MobEffect) (Object)this) == MobEffects.WITHER)
            {
                return original.call(entity,source,2 * ev / 3);
            }
        }
        return original.call(entity,source,ev);
    }
}
