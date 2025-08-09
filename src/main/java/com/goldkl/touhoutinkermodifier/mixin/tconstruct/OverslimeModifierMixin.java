package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

@Mixin(OverslimeModifier.class)
public class OverslimeModifierMixin {
    @ModifyConstant(
            method = "<clinit>",
            constant = {
                    @Constant(floatValue = 32767.0F),
            }
    )
    private static float modifyMaxValue(float constant) {
        return 4000000.0f;
    }
}
