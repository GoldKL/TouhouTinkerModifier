package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface BetterCombatAtackRangeHook {
    //该方法在客户端执行，注意数据同步
    //该钩子仅为了适配更好的战斗，原版的攻击距离请使用net.minecraftforge.common.ForgeMod.ENTITY_REACH来处理
    //tool_attack_range表示基础攻击距离，但返回的amount是去修改的，注意区分
    //baseamount = 0 则为加法,baseamount = 1则为乘法
    double attackermodifyBetterCombatAtackRange(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean IsAdd, double amount, double tool_attack_range, InteractionHand hand);
    record AllMerger(Collection<BetterCombatAtackRangeHook> modules) implements BetterCombatAtackRangeHook {
        @Override
        public double attackermodifyBetterCombatAtackRange(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean IsAdd, double amount,double tool_attack_range,InteractionHand hand) {
            for (BetterCombatAtackRangeHook module : modules) {
                amount = module.attackermodifyBetterCombatAtackRange(tool, modifier, context, slotType,  IsAdd, amount,tool_attack_range,hand);
            }
            return amount;
        }
    }
    static double attackermodifyBetterCombatAtackRange(ModuleHook<BetterCombatAtackRangeHook> hook, EquipmentContext context, boolean IsAdd, double amount,double tool_attack_range,InteractionHand hand) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    amount = entry.getHook(hook).attackermodifyBetterCombatAtackRange(toolStack, entry, context, slotType, IsAdd, amount,tool_attack_range, hand);
                }
            }
        }
        return amount;
    }
}
