package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.api.IAbstractSpell;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TooltipsUtils.class)
public class TooltipsUtilsMixin {
    @WrapOperation(method = "formatActiveSpellTooltip",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    private static int formatActiveSpellTooltipmixin(AbstractSpell instance, int level, Operation<Integer> original, @Local(argsOnly = true) LocalPlayer player)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, player);
    }
    @WrapOperation(method = "formatScrollTooltip",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    private static int formatScrollTooltipmixin(AbstractSpell instance, int level, Operation<Integer> original, @Local(argsOnly = true) LocalPlayer player)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, player);
    }
}
