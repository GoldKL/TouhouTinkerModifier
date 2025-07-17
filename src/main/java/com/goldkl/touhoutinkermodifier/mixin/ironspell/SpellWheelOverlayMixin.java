package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.api.IAbstractSpell;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.gui.overlays.SpellWheelOverlay;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellWheelOverlay.class)
public class SpellWheelOverlayMixin {
    @WrapOperation(method = "render",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    private int rendermixin(AbstractSpell instance, int level, Operation<Integer> original)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, Minecraft.getInstance().player);
    }
}
