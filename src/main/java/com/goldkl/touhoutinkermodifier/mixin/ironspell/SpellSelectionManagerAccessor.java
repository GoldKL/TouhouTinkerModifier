package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpellSelectionManager.class)
public interface SpellSelectionManagerAccessor {
    @Accessor(value = "selectionIndex",remap = false)
    void setSelectionIndex(int i);
    @Accessor(value = "selectionValid",remap = false)
    boolean getSelectionValid();
    @Accessor(value = "selectionValid",remap = false)
    void setSelectionValid(boolean b);
}
