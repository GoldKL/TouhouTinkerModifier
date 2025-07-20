package com.goldkl.touhoutinkermodifier.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffects.class)
public class MobEffectsMixin {
    @WrapOperation(method = "<clinit>",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffects;register(ILjava/lang/String;Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffect;"))
    private static MobEffect mobEffectmixin(int id, String name, MobEffect effect, Operation<MobEffect> original)
    {
        if(id == 15)
        {
            effect.addAttributeModifier(Attributes.FOLLOW_RANGE, "0d17749c-49c6-4636-847e-8528bfc8eebf", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
        return original.call(id, name, effect);
    }
}
