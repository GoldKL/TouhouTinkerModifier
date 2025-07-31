package com.goldkl.touhoutinkermodifier.hook;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
//玩家以外的实体并不会自动双端同步buff，所以请保证在双端执行结果相同
public interface EntityEffectApplicableModifierHook {
    boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity, boolean notApplicable);

    record AllMerger(Collection<EntityEffectApplicableModifierHook> modules) implements EntityEffectApplicableModifierHook {
        public AllMerger(Collection<EntityEffectApplicableModifierHook> modules) {
            this.modules = modules;
        }

        public boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity, boolean notApplicable) {
            for(EntityEffectApplicableModifierHook module : this.modules) {
                boolean NotApplicable = module.isApplicable(tool, entry, slot, instance,entity, notApplicable);
                if (NotApplicable) {
                    return true;
                }
            }

            return notApplicable;
        }

        public Collection<EntityEffectApplicableModifierHook> modules() {
            return this.modules;
        }
    }
}
