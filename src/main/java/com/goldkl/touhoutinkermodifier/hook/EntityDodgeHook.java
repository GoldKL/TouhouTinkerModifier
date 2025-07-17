package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.Collection;

public interface EntityDodgeHook {
    default boolean CanDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,@Nullable Entity attacker, @Nullable Entity directattacker){
        return false;
    }
    default void OnDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,@Nullable Entity attacker, @Nullable Entity directattacker){

    }
    record AllMerger(Collection<EntityDodgeHook> modules) implements EntityDodgeHook {
        @Override
        public boolean CanDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,@Nullable Entity attacker, @Nullable Entity directattacker) {
            boolean canDodge = false;
            for (EntityDodgeHook module : modules) {
                canDodge = module.CanDodge(tool, modifier, context, slotType, attacker,directattacker);
                if (canDodge) {
                    break;
                }
            }
            return canDodge;
        }
        @Override
        public void OnDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,@Nullable Entity attacker, @Nullable Entity directattacker) {
            for (EntityDodgeHook module : modules) {
                module.OnDodge(tool, modifier, context, slotType,attacker, directattacker);
            }
        }
    }
}
