package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class TerriblesouvenirModifier extends Modifier implements ModifierTraitHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        for(ModifierEntry it : context.getModifierList())
        {
            if(it.matches(TagsRegistry.ModifiersTag.Chireiden)
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
