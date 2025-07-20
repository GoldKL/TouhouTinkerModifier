package com.goldkl.touhoutinkermodifier.hook;

import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface EntityHealHook {
    default float modifyhealTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, float baseheal, float heal)
    {
        return heal;
    }
    record AllMerger(Collection<EntityHealHook> modules) implements EntityHealHook {
        @Override
        public float modifyhealTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, float baseheal, float heal) {
            for (EntityHealHook module : modules) {
                heal = module.modifyhealTaken(tool, modifier, context, slotType, baseheal, heal);
                if (heal <= 0) {
                    break;
                }
            }
            return heal;
        }
    }
    static float modifyhealTaken(EquipmentContext context, float baseheal, float heal) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    heal = entry.getHook(ModifierHooksRegistry.ENTITY_HEAL_HOOK).modifyhealTaken(toolStack, entry, context, slotType, baseheal,heal);
                    if (heal <= 0) {
                        return 0;
                    }
                }
            }
        }
        return heal;
    }
}
