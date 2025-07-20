package com.goldkl.touhoutinkermodifier.modifiers;

import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class BengshancaijiModifier extends NoLevelsModifier implements ToolStatsModifierHook {
    //崩山彩极：红美铃
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (context.hasTag(TinkerTags.Items.MELEE)) {
            ToolStats.ATTACK_DAMAGE.multiply(builder, 2);
        }
        if (context.hasTag(TinkerTags.Items.HARVEST)) {
            ToolStats.MINING_SPEED.multiply(builder, 2);
        }
        if (context.hasTag(TinkerTags.Items.RANGED)) {
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 2);
        }
    }
}
