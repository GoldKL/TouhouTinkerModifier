package com.goldkl.touhoutinkermodifier.modifiers;

import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Iterator;


public class OverpowermqModifier extends Modifier implements ToolStatsModifierHook {
    //土水符「史莱姆的总攻」：帕秋莉
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = getlevelcount(context);
        float amount = modifier.getLevel()*level*0.1f;
        if (amount > 0) {
            if (context.hasTag(TinkerTags.Items.MELEE)) {
                ToolStats.ATTACK_DAMAGE.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.HARVEST)) {
                ToolStats.MINING_SPEED.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.ARMOR)) {
                ToolStats.ARMOR.percent(builder, amount);
                ToolStats.ARMOR_TOUGHNESS.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.RANGED)) {
                ToolStats.VELOCITY.percent(builder, amount);
            }
        }
    }
    private int getlevelcount(IToolContext tool)
    {
        Iterator<ModifierEntry> it = tool.getModifiers().iterator();
        int level = 0;
        while(it.hasNext())
        {
            ModifierEntry entry = it.next();
            if (ModifierManager.isInTag(entry.getId(), TinkerTags.Modifiers.OVERSLIME_FRIEND)) {
                level += entry.getLevel();
            }
        }
        return level;
    }
}
