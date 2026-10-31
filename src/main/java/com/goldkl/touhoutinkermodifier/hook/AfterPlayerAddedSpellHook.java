package com.goldkl.touhoutinkermodifier.hook;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AfterPlayerAddedSpellHook {
    void afterPlayerAddedSpell(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager);
    record AllMerger(Collection<AfterPlayerAddedSpellHook> modules) implements AfterPlayerAddedSpellHook {
        @Override
        public void afterPlayerAddedSpell(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager) {
            for (AfterPlayerAddedSpellHook module : modules) {
                module.afterPlayerAddedSpell(tool, modifier, context, slotType, manager);
            }
        }
    }
}
