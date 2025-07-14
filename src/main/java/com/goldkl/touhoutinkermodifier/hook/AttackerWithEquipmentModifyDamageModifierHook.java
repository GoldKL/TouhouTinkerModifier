package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AttackerWithEquipmentModifyDamageModifierHook {
    float attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, float amount, boolean isDirectDamage);
    record AllMerger(Collection<AttackerWithEquipmentModifyDamageModifierHook> modules) implements AttackerWithEquipmentModifyDamageModifierHook {
        @Override
        public float attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, float amount, boolean isDirectDamage) {
            for (AttackerWithEquipmentModifyDamageModifierHook module : modules) {
                amount = module.attackermodifyDamageTaken(tool, modifier, context, slotType, source, baseamount, amount, isDirectDamage);
                if (amount <= 0) {
                    break;
                }
            }
            return amount;
        }
    }
    static float attackermodifyDamageTaken(ModuleHook<AttackerWithEquipmentModifyDamageModifierHook> hook, EquipmentContext context, DamageSource source, float baseamount, float amount, boolean isDirectDamage) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    amount = entry.getHook(hook).attackermodifyDamageTaken(toolStack, entry, context, slotType, source, baseamount, amount, isDirectDamage);
                    if (amount < 0) {
                        return 0;
                    }
                }
            }
        }
        return amount;
    }
}
