package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;

@Mixin(BasicModifier.class)
public interface BasicModifierAccessor {
    @Accessor(value = "levelDisplay" ,remap = false)
    ModifierLevelDisplay getLevelDisplay();
}
