package com.goldkl.touhoutinkermodifier.hook;

import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface MeleeDamagePercentModifierHook {
    default void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {

    }
    record AllMerger(Collection<MeleeDamagePercentModifierHook> modules) implements MeleeDamagePercentModifierHook {
        @Override
        public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
        {
            for (MeleeDamagePercentModifierHook module : modules) {
                module.getMeleeDamageModifier(tool, modifier, context, baseDamage, damage, damagemodifier);
            }
        }
    }
}
