package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class OverturningallunderheavenModifier extends Modifier{
    /*@Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        int num = 0;
        for (MaterialVariant material : context.getMaterials()) {
            if(MaterialRegistry.getInstance().isInTag(material.getId(), TagsRegistry.MaterialsTag.Seijakijin)){
                continue;
            }
            num += 3 - MaterialRegistry.getInstance().getMaterial(material.getId()).getTier();
        }
        float amount = level * num * 0.2f;
        if (amount > 0) {
            if (context.hasTag(TinkerTags.Items.MELEE)) {
                ToolStats.ATTACK_DAMAGE.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.HARVEST)) {
                ToolStats.MINING_SPEED.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.ARMOR)) {
                ToolStats.ARMOR.percent(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.RANGED)) {
                ToolStats.VELOCITY.percent(builder, amount);
            }
        }
    }*/
}
