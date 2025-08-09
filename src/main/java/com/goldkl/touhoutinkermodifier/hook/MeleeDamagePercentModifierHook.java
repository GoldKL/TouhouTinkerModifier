package com.goldkl.touhoutinkermodifier.hook;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface MeleeDamagePercentModifierHook {
    default float getMeleeAdd(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float add)
    {
        return add;
    }
    default float getMeleePercent(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float percent)
    {
        return percent;
    }
    default float getMeleeMultiply(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage)
    {
        return 1.0f;
    }
    default float getMeleeFixed(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float fixed)
    {
        return fixed;
    }
    /** Merger that runs all nested hooks */
    record AllMerger(Collection<MeleeDamagePercentModifierHook> modules) implements MeleeDamagePercentModifierHook {
        @Override
        public float getMeleeAdd(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float add)
        {
            for (MeleeDamagePercentModifierHook module : modules) {
                add = module.getMeleeAdd(tool, modifier, context, baseDamage, damage, add);
            }
            return add;
        }
        @Override
        public float getMeleePercent(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float percent)
        {
            for (MeleeDamagePercentModifierHook module : modules) {
                percent = module.getMeleePercent(tool, modifier, context, baseDamage, damage, percent);
            }
            return percent;
        }
        @Override
        public float getMeleeMultiply(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage)
        {
            float multiplier = 1.0f;
            for (MeleeDamagePercentModifierHook module : modules) {
                multiplier *= module.getMeleeMultiply(tool, modifier, context, baseDamage, damage);
            }
            return multiplier;
        }
        @Override
        public float getMeleeFixed(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float fixed)
        {
            for (MeleeDamagePercentModifierHook module : modules) {
                fixed = module.getMeleeFixed(tool, modifier, context, baseDamage, damage, fixed);
            }
            return fixed;
        }
    }
}
