package com.goldkl.touhoutinkermodifier.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.goldkl.touhoutinkermodifier.tracking.ModifierEvent.NotFightingTime;

public class NaturalgraceModifier extends Modifier implements InventoryTickModifierHook {
    //毛玉：自然恩惠
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide || !isCorrectSlot)return;
        holder.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            if(data.getInt(NotFightingTime) > 60){
                int level = modifier.getLevel();
                int num = 80 / level;
                if(num == 0 || holder.tickCount%num == 0){
                    holder.heal(1);
                }
            }
        });
    }
}
