package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityEffectAddModifierHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SonanokaModifier extends Modifier implements EntityEffectAddModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_EFFECT_APPLICABLE_HURT);
    }

    @Override
    public void onApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity) {
        if(!(instance.getEffect() == MobEffects.BLINDNESS || instance.getEffect() == MobEffects.DARKNESS))return;
        int levelmax = TTMEntityUtils.getModifiertotalLevel(entity,ModifierIds.sonanoka) - 1;
        int nowlevel = -1;
        if(entity.hasEffect(MobeffectRegistry.SONANOKA.get()))
        {
            nowlevel = entity.getEffect(MobeffectRegistry.SONANOKA.get()).getAmplifier();
        }
        if(nowlevel >= levelmax)return;
        entity.removeEffect(MobeffectRegistry.SONANOKA.get());
        entity.addEffect(new MobEffectInstance(MobeffectRegistry.SONANOKA.get(), 80 , nowlevel + 1,false, true));
    }
}
