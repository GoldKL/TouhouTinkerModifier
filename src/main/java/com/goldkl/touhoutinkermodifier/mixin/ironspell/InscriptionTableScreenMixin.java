package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.api.IAbstractSpell;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InscriptionTableScreen.class)
public class InscriptionTableScreenMixin {
    @WrapOperation(method = "renderLorePage",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    private int formatScrollTooltipmixin(AbstractSpell instance, int level, Operation<Integer> original)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, Minecraft.getInstance().player);
    }
}
