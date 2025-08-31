package com.goldkl.touhoutinkermodifier.hook;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AfterMeleeHitModifierHook {
    //与匠魂原版不同的是，这里使用的是理论伤害值，而不是实际伤害值
    default void afterMeleeHitWithTheoryDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {}
    record AllMerger(Collection<AfterMeleeHitModifierHook> modules) implements AfterMeleeHitModifierHook {
        @Override
        public void afterMeleeHitWithTheoryDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
            for (AfterMeleeHitModifierHook module : modules) {
                module.afterMeleeHitWithTheoryDamage(tool, modifier, context, damage);
            }
        }

    }
}
