package com.goldkl.touhoutinkermodifier.hook;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

//该钩子双端执行，注意双端同步
public interface MagicAffinityHook {
    default int getMagicAffinity(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, AbstractSpell spell, int baseLevel, int totalLevel){
        return totalLevel;
    }
    record AllMerger(Collection<MagicAffinityHook> modules) implements MagicAffinityHook {
        @Override
        public int getMagicAffinity(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, AbstractSpell spell,int baseLevel,int totalLevel) {
            for (MagicAffinityHook module : modules) {
                totalLevel = module.getMagicAffinity(tool,modifier,context,slotType,spell,baseLevel,totalLevel);
            }
            return totalLevel;
        }
    }
}
