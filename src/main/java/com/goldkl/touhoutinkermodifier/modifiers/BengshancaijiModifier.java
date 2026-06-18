package com.goldkl.touhoutinkermodifier.modifiers;

import pyre.tinkerslevellingaddon.ImprovableModifier;
import pyre.tinkerslevellingaddon.config.Config;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
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
    public static ModifierId IMPROVABLE = new ModifierId("tinkerslevellingaddon","improvable");
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if(context.getModifier(IMPROVABLE) != ModifierEntry.EMPTY){
            IModDataView data = context.getPersistentData();
            int currentLevel = data.getInt(ImprovableModifier.LEVEL_KEY);
            if(currentLevel >= Config.maxLevel.get()){
                if (context.hasTag(TinkerTags.Items.MELEE)) {
                    ToolStats.ATTACK_DAMAGE.multiply(builder, 1.5);
                }
                if (context.hasTag(TinkerTags.Items.HARVEST)) {
                    ToolStats.MINING_SPEED.multiply(builder, 1.5);
                }
                if (context.hasTag(TinkerTags.Items.RANGED)) {
                    ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.5);
                }
            }
        }
    }
}
