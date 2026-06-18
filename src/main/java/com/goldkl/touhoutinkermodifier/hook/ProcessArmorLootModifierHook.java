package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
import java.util.List;

/** come from TC. */
public interface ProcessArmorLootModifierHook {

  void processArmorLoot(IToolStackView tool, ModifierEntry modifier, LivingEntity origin, List<ItemStack> generatedLoot, LootContext context, EquipmentSlot slot);

  record AllMerger(Collection<ProcessArmorLootModifierHook> modules) implements ProcessArmorLootModifierHook {
    @Override
    public void processArmorLoot(IToolStackView tool, ModifierEntry modifier, LivingEntity origin, List<ItemStack> generatedLoot, LootContext context, EquipmentSlot slot) {
      for (ProcessArmorLootModifierHook module : modules) {
        module.processArmorLoot(tool, modifier,origin, generatedLoot, context, slot);
      }
    }
  }
}
