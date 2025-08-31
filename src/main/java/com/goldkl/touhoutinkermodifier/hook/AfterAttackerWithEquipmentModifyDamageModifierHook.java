package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AfterAttackerWithEquipmentModifyDamageModifierHook {
    void afterattackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage);
    record AllMerger(Collection<AfterAttackerWithEquipmentModifyDamageModifierHook> modules) implements AfterAttackerWithEquipmentModifyDamageModifierHook {
        @Override
        public void afterattackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
            for (AfterAttackerWithEquipmentModifyDamageModifierHook module : modules) {
                module.afterattackermodifyDamageTaken(tool, modifier, context, slotType, source, amount, isDirectDamage);
            }
        }
    }
    static void afterattackermodifyDamageTaken(ModuleHook<AfterAttackerWithEquipmentModifyDamageModifierHook> hook, EquipmentContext context, DamageSource source, float amount,  boolean isDirectDamage) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    entry.getHook(hook).afterattackermodifyDamageTaken(toolStack, entry, context, slotType, source, amount, isDirectDamage);
                }
            }
        }
    }
}
