package com.goldkl.touhoutinkermodifier.modifiers;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.special.CapacityBarHook;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class OverroseModifier extends Modifier implements InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        hookBuilder.addModule(StatBoostModule.add(OverslimeModule.OVERSLIME_STAT).eachLevel(150));
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide && holder.tickCount % 20 == 0 && holder.getUseItem() != stack) {
            CapacityBarHook bar = OverslimeModule.INSTANCE;
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
            double percent = (holder.hasEffect(YHEffects.UNCONSCIOUS.get()) ? 2.0 : 1.0) * (0.8 + entry.getLevel() * 0.2);
            if (entry.getLevel() > 0 && bar.getAmount(tool) < bar.getCapacity(tool, entry)) {
                int addover = (int) (bar.getCapacity(tool, entry) * percent);
                bar.addAmount(tool, modifier, addover);
            }
        }
    }
}
