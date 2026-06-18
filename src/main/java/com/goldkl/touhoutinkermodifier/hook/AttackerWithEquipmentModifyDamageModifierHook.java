package com.goldkl.touhoutinkermodifier.hook;

import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AttackerWithEquipmentModifyDamageModifierHook {
    void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage);
    record AllMerger(Collection<AttackerWithEquipmentModifyDamageModifierHook> modules) implements AttackerWithEquipmentModifyDamageModifierHook {
        @Override
        public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
            for (AttackerWithEquipmentModifyDamageModifierHook module : modules) {
                module.attackermodifyDamageTaken(tool, modifier, target, context, slotType, source, baseamount, damageModifier, isDirectDamage);
            }
        }
    }
    static void attackermodifyDamageTaken(ModuleHook<AttackerWithEquipmentModifyDamageModifierHook> hook, LivingEntity target, EquipmentContext context, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getValidTool(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    entry.getHook(hook).attackermodifyDamageTaken(toolStack, entry, target, context, slotType, source, baseamount, damageModifier, isDirectDamage);
                }
            }
        }
    }
}
