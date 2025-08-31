package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
//玩家以外的实体并不会自动双端同步buff，所以请保证在双端执行结果相同（存疑，直接看代码感觉是没有问题的，但保持同步总是好的）
public interface EntityEffectAddModifierHook {
    void onApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity);

    record AllMerger(Collection<EntityEffectAddModifierHook> modules) implements EntityEffectAddModifierHook {
        public void onApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity) {
            for(EntityEffectAddModifierHook module : this.modules) {
                module.onApplicable(tool, entry, slot, instance, entity);
            }
        }
    }
}
