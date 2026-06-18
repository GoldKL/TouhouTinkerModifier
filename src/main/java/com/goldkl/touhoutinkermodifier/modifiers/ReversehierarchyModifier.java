package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.data.material.TTMMaterialIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class ReversehierarchyModifier extends Modifier implements ToolStatsModifierHook, ModifierTraitHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.MODIFIER_TRAITS);
    }
    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, ModifierTraitHook.TraitBuilder builder, boolean firstEncounter) {
        for (MaterialVariant material : context.getMaterials()) {
            if(MaterialRegistry.getInstance().isInTag(material.getId(), TagsRegistry.MaterialsTag.Seijakijin)){
                continue;
            }
            if(MaterialRegistry.getInstance().getMaterial(material.getId()).getTier() >= 5){
                return;
            }
        }
        for (MaterialVariant material : context.getMaterials()) {
            if(material.matches(TTMMaterialIds.kedama)){
                builder.add(TTMModifierIds.naturalwarmog, modifier.getLevel());
            }
            if(material.matches(MaterialIds.string)){
                builder.add(TTMModifierIds.gallowsstring, modifier.getLevel());
            }
            if(material.matches(MaterialIds.leather)){
                builder.add(TTMModifierIds.retanned,1);
                if (context.hasTag(TinkerTags.Items.MELEE) || context.hasTag(TinkerTags.Items.RANGED)) {
                    builder.add(TTMModifierIds.pickledgrit, modifier.getLevel());
                }
                if (context.hasTag(TinkerTags.Items.ARMOR)) {
                    builder.add(TTMModifierIds.passivatingforcetrauma, modifier.getLevel());
                }
            }
        }
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        int num_0 = 0;
        int num_4 = 0;
        boolean num_5 = false;
        for (MaterialVariant material : context.getMaterials()) {
            if(MaterialRegistry.getInstance().isInTag(material.getId(), TagsRegistry.MaterialsTag.Seijakijin)){
                continue;
            }
            if(MaterialRegistry.getInstance().isInTag(material.getId(), TagsRegistry.MaterialsTag.Seijakijin_Tier0)){
                continue;
            }
            if(MaterialRegistry.getInstance().getMaterial(material.getId()).getTier() == 0) num_0 ++;
            if(MaterialRegistry.getInstance().getMaterial(material.getId()).getTier() >= 4) num_4 ++;
            if(MaterialRegistry.getInstance().getMaterial(material.getId()).getTier() >= 5) num_5 = true;
        }
        if (num_4 > 0) {
            float amount = Math.max(0,1 - num_4 * 0.5f);
            if (context.hasTag(TinkerTags.Items.DURABILITY)) {
                ToolStats.DURABILITY.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.MELEE)) {
                ToolStats.ATTACK_DAMAGE.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.HARVEST)) {
                ToolStats.MINING_SPEED.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.ARMOR)) {
                ToolStats.ARMOR.multiply(builder, amount);
            }
            if (context.hasTag(TinkerTags.Items.RANGED)) {
                ToolStats.VELOCITY.multiply(builder, amount);
            }
        }
        if(!num_5 && num_0 > 0){
            float amount = level * num_0 * 0.25f;
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
    }
}
