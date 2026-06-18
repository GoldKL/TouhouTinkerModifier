package com.goldkl.touhoutinkermodifier.hook;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
//该钩子双端执行，且只有玩家使用，注意双端同步
public interface AddPlayerMagicHook {
    @Nullable
    ArrayList<SpellData> getmagiclist(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager);
    record AllMerger(Collection<AddPlayerMagicHook> modules) implements AddPlayerMagicHook {
        @Override
        public ArrayList<SpellData> getmagiclist(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager) {
            ArrayList<SpellData>list = new ArrayList<>();
            for (AddPlayerMagicHook module : modules) {
                ArrayList<SpellData>newlist = module.getmagiclist(tool, modifier, context, slotType, manager);
                if(newlist != null)
                    list.addAll(newlist);
            }
            return list;
        }
    }
}
