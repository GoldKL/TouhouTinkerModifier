package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpellContainer.class)
public class SpellContainerMixin {
    @Inject(method = "addSpellAtIndex(Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;IIZLnet/minecraft/world/item/ItemStack;)Z"
            ,at = @At("HEAD")
            ,cancellable = true
            ,remap = false)
    void addSpellAtIndexmixin(AbstractSpell spell, int level, int index, boolean locked, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir)
    {
        try{
            if(spell == null || !spell.isEnabled())
            {
                cir.setReturnValue(false);
                //禁止被ban的法术被添加
            }
        }catch(Exception ignore){

        }
    }
}
