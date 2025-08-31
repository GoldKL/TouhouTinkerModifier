package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface AttackerWithEquipmentModifyDamageModifierHook {
    void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage);
    record AllMerger(Collection<AttackerWithEquipmentModifyDamageModifierHook> modules) implements AttackerWithEquipmentModifyDamageModifierHook {
        @Override
        public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
            for (AttackerWithEquipmentModifyDamageModifierHook module : modules) {
                module.attackermodifyDamageTaken(tool, modifier, target, context, slotType, source, baseamount, damageModifier, isDirectDamage);
            }
        }
    }
    static void attackermodifyDamageTaken(ModuleHook<AttackerWithEquipmentModifyDamageModifierHook> hook, LivingEntity target, EquipmentContext context, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken() && ModifierUtil.validArmorSlot(toolStack,slotType)) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    entry.getHook(hook).attackermodifyDamageTaken(toolStack, entry, target, context, slotType, source, baseamount, damageModifier, isDirectDamage);
                }
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
        public DamageModifier(DamageModifier other) {
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
