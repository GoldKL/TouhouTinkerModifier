package com.goldkl.touhoutinkermodifier.modifiers;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.special.CapacityBarHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.DurabilityShieldModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class KagakuSeikiModifier extends NoLevelsModifier implements CapacityBarHook, ToolDamageModifierHook, DurabilityDisplayModifierHook {
    //科学世纪：宇佐见莲子
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(ToolEnergyCapability.ENERGY_HANDLER);
        hookBuilder.addModule(StatBoostModule.add(ToolEnergyCapability.MAX_STAT).flat(100000));
        hookBuilder.addHook(this, ModifierHooks.CAPACITY_BAR,ModifierHooks.TOOL_DAMAGE, ModifierHooks.DURABILITY_DISPLAY);
    }
    @Override
    public int getPriority(){
        return 125;
    }
    @Override
    public int getAmount(IToolStackView tool) {
        return ToolEnergyCapability.getEnergy(tool);
    }

    @Override
    public int getCapacity(IToolStackView tool, ModifierEntry entry) {
        return ToolEnergyCapability.getMaxEnergy(tool);
    }

    @Override
    public void setAmount(IToolStackView tool, ModifierEntry entry, int amount) {
        ToolEnergyCapability.setEnergy(tool, amount);
    }
    //code from DurabilityShieldModule
    public static int onDamageTool(CapacityBarHook bar, IToolStackView tool, ModifierEntry modifier, int amount) {
        int shield = bar.getAmount(tool);
        int true_shield = shield / 250;
        if (true_shield > 0) {
            // if we have more overslime than amount, remove some overslime
            if (true_shield >= amount) {
                bar.setAmount(tool, modifier, shield - amount * 250);
                return 0;
            }
            // amount is more than overslime, reduce and clear overslime
            amount -= true_shield;
            bar.setAmount(tool, modifier, shield - true_shield * 250);
        }
        else if(shield > 0){
            bar.setAmount(tool, modifier, 0);
        }
        return amount;
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return onDamageTool(modifier.getHook(ModifierHooks.CAPACITY_BAR), tool, modifier, amount);
    }

    @Override
    public int getDurabilityWidth(IToolStackView tool, ModifierEntry modifier) {
        CapacityBarHook bar = modifier.getHook(ModifierHooks.CAPACITY_BAR);
        int shield = bar.getAmount(tool);
        if (shield > 0) {
            return DurabilityDisplayModifierHook.getWidthFor(shield, bar.getCapacity(tool, modifier));
        }
        return 0;
    }

    @Nullable
    @Override
    public Boolean showDurabilityBar(IToolStackView tool, ModifierEntry modifier) {
        return modifier.getHook(ModifierHooks.CAPACITY_BAR).getAmount(tool) > 0 ? true : null;
    }

    @Override
    public int getDurabilityRGB(IToolStackView tool, ModifierEntry modifier) {
        if (modifier.getHook(ModifierHooks.CAPACITY_BAR).getAmount(tool) > 0) {
            return 0xffddaf;
        }
        return -1;
    }
}
