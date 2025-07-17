package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.api.IAbstractSpell;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.item.CastingItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CastingItem.class)
public class CastingItemMixin {
    @WrapOperation(method = "use",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"))
    private int rendermixin(AbstractSpell instance, int level, Operation<Integer> original,@Local(argsOnly = true) Player player)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, player);
    }
}

