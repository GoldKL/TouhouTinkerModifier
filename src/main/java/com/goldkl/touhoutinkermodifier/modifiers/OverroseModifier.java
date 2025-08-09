package com.goldkl.touhoutinkermodifier.modifiers;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class OverroseModifier extends Modifier implements InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        hookBuilder.addModule(StatBoostModule.add(OverslimeModifier.OVERSLIME_STAT).eachLevel(150));
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide && holder.tickCount % 20 == 0 && holder.getUseItem() != stack) {
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
            double percent = holder.hasEffect(YHEffects.UNCONSCIOUS.get()) ? 2.0 : 1.0;
            if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                int addover = (int) (overslime.getShieldCapacity(tool, entry) * percent);
                overslime.addOverslime(tool, entry, addover);
            }
        }
    }
}
