package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class BatteryloadModifier extends Modifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(ToolEnergyCapability.ENERGY_HANDLER);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }
    @Override
    public int getPriority(){
        return 0;
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        boolean flag = SecretsealingclubModifier.SecretSealingClubcanUse(context);
        ToolEnergyCapability.MAX_STAT.add(builder,level * 25000);
        if(flag || context.hasTag(TagsRegistry.ItemsTag.TECHNOLOGY))
        {
            if(flag)
            {
                ToolEnergyCapability.MAX_STAT.multiply(builder,2);
            }
            float num = ToolEnergyCapability.MAX_STAT.clamp(builder.getStat(ToolEnergyCapability.MAX_STAT)) / 50000f;
            float amount = Math.min(num,level * 4) * 0.1f;
            if (amount > 0) {
                if (context.hasTag(TinkerTags.Items.MELEE)) {
                    ToolStats.ATTACK_DAMAGE.percent(builder, amount);
                    ToolStats.ATTACK_SPEED.percent(builder, amount);
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
    }
}
