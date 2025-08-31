package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.EffectApplicableModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modules.armor.CounterModule;

public class LoveburiedembersModifier extends Modifier implements OnAttackedModifierHook, EffectApplicableModifierHook {
    //恋之埋火：古明地恋（仅护甲）
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED, EtSTLibHooks.EFFECT_APPLICABLE);
    }
    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        Entity attacker = source.getEntity();
        if (isDirectDamage && attacker != null) {
            LivingEntity defender = context.getEntity();
            float level = CounterModule.getLevel(tool, modifier, slotType, defender);
            float value = tool.getStats().get(ToolStats.ARMOR);
            attacker.hurt(defender.damageSources().thorns(defender), value * (1 + 0.1f*level));
        }
    }

    @Override
    public Boolean isApplicable(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, MobEffectInstance instance, Boolean notApplicable) {
        return notApplicable||instance.getEffect() == MobEffects.MOVEMENT_SLOWDOWN;
    }
}
