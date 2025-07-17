package com.goldkl.touhoutinkermodifier.mixin.sakuratinker;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.FountainMagicModifier;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(FountainMagicModifier.class)
public class FountainMagicModifierMixin {
    @WrapOperation(method = "onEquip",at = @At(value = "INVOKE", target = "Lcom/ssakura49/sakuratinker/compat/IronSpellBooks/modifiers/FountainMagicModifier;addAttributeModifier(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/util/UUID;Ljava/lang/String;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)V"),remap = false)
    private void addAttributeModifier(FountainMagicModifier instance, Player player, Attribute attribute, UUID uuid, String name, double value, AttributeModifier.Operation operation, Operation<Void> original)
    {
        if(attribute == AttributeRegistry.MAX_MANA.get())
        {
            original.call(instance, player, PerkAttributes.MAX_MANA.get(),uuid,name,value,operation);
        }
        else if(attribute == AttributeRegistry.MANA_REGEN.get())
        {
            original.call(instance, player, PerkAttributes.MANA_REGEN_BONUS.get(),uuid,name,value,operation);
        }
        else
        {
            original.call(instance, player, attribute,uuid,name,value,operation);
        }
    }
    @WrapOperation(method = "onUnequip",at = @At(value = "INVOKE", target = "Lcom/ssakura49/sakuratinker/compat/IronSpellBooks/modifiers/FountainMagicModifier;removeAttributeModifier(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/util/UUID;)V"),remap = false)
    private void removeAttributeModifier(FountainMagicModifier instance, Player player, Attribute attribute, UUID uuid, Operation<Void> original)
    {
        if(attribute == AttributeRegistry.MAX_MANA.get())
        {
            original.call(instance, player, PerkAttributes.MAX_MANA.get(),uuid);
        }
        else if(attribute == AttributeRegistry.MANA_REGEN.get())
        {
            original.call(instance, player, PerkAttributes.MANA_REGEN_BONUS.get(),uuid);
        }
        else
        {
            original.call(instance, player, attribute,uuid);
        }
    }
}
