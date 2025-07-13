package com.goldkl.touhoutinkermodifier.mixin.arsnouveau;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArsNouveauAPI.class)
public class ArsNouveauAPIMixin {
    @Shadow(remap = false)
    public static boolean ENABLE_DEBUG_NUMBERS;
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void EnableDebugNumbers(CallbackInfo ci) {
        ENABLE_DEBUG_NUMBERS = true;
    }
}
