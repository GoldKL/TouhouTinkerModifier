package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class SevenluminarieswizardModifier extends Modifier {
    //七曜魔法使：帕秋莉
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributesRegistry.MANA_COST_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).eachLevel(0.1f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.FIRE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.ICE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.LIGHTNING_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.BLOOD_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.NATURE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.EVOCATION_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.HOLY_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.sevenluminarieswizard).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.05f));

    }
}
