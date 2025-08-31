package com.goldkl.touhoutinkermodifier.hook;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
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
    class DamageModifier{
        float baseamount;
        float add;
        float percent;
        float multiply;
        float fixed;
        public float getBaseamount() {
            return baseamount;
        }
        public float getAdd() {
            return add;
        }
        public float getPercent() {
            return percent;
        }
        public float getMultiply() {
            return multiply;
        }
        public float getFixed() {
            return fixed;
        }
        public float getamount(){
            return (baseamount + add) * (1 + percent) * multiply + fixed;
        }
        public DamageModifier(float baseamount) {
            this.baseamount = baseamount;
            this.add = 0;
            this.percent = 0;
            this.multiply = 1;
            this.fixed = 0;
        }
        public DamageModifier(AttackerWithEquipmentModifyDamageModifierHook.DamageModifier other) {
            this.baseamount = other.baseamount;
            this.add = other.add;
            this.percent = other.percent;
            this.multiply = other.multiply;
            this.fixed = other.fixed;
        }
        public void addAdd(float add) {
            this.add += add;
        }
        public void addPercent(float percent) {
            this.percent += percent;
        }
        public void addMultiply(float multiply) {
            this.multiply *= multiply;
        }
        public void addFixed(float fixed) {
            this.fixed += fixed;
        }
        public void setBaseamount(float baseamount) {
            this.baseamount = baseamount;
        }
        public void setAdd(float add) {
            this.add = add;
        }
        public void setPercent(float percent) {
            this.percent = percent;
        }
        public void setMultiply(float multiply) {
            this.multiply = multiply;
        }
        public void setFixed(float fixed) {
            this.fixed = fixed;
        }
    }
}
