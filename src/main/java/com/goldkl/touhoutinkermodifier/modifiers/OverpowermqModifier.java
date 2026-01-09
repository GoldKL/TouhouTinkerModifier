package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;



public class OverpowermqModifier extends Modifier implements ToolStatsModifierHook {
    //土水符「史莱姆的总攻」：帕秋莉
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
        hookBuilder.addModule(StatBoostModule.add(OverslimeModule.OVERSLIME_STAT).eachLevel(100));
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = TTMEntityUtils.gettotallevelwithtag(context, TinkerTags.Modifiers.OVERSLIME_FRIEND);
        float amount = 1.0f + modifier.getLevel()*level*0.1f;
        if (amount > 0) {
            if (context.hasTag(TinkerTags.Items.MELEE)) {
                ToolStats.ATTACK_DAMAGE.multiply(builder, amount);
                ToolStats.ATTACK_SPEED.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.HARVEST)) {
                ToolStats.MINING_SPEED.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.ARMOR)) {
                ToolStats.ARMOR.multiply(builder, amount);
                ToolStats.ARMOR_TOUGHNESS.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.RANGED)) {
                ToolStats.VELOCITY.multiply(builder, amount);
            }
        }
    }
}
