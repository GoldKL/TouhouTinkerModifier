package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class OverthinkingModifier extends Modifier implements ModifierTraitHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS);
        hookBuilder.addModule(StatBoostModule.add(OverslimeModifier.OVERSLIME_STAT).eachLevel(10));
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        for(ModifierEntry it : context.getModifierList())
        {
            if(it.matches(TinkerTags.Modifiers.OVERSLIME_FRIEND)
                    && !it.matches(TTMModifierIds.terriblesouvenir)
                    && !it.matches(TTMModifierIds.overthinking)
                    && !it.matches(TTMModifierIds.komeijisisters)
                    && !TTMEntityUtils.isNolevelModifier(it))
            {
                builder.add(it.getId(), modifier.getLevel());
            }
        }
    }
}
