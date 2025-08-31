package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import io.redspace.ironsspellbooks.api.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Utils.class)
public class UtilsMixin {
    @Inject(method = "softCapFormula",at = @At("RETURN"),cancellable = true,remap = false)
    private static void softCapFormulaMixin(double x, CallbackInfoReturnable<Double> cir)
    {
        cir.setReturnValue(x >= 1 ? 2 - 1 / x : x );
    }
}
