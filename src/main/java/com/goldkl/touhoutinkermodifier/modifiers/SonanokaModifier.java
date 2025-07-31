package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityEffectApplicableModifierHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class SonanokaModifier extends Modifier implements EntityEffectApplicableModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_EFFECT_APPLICABLE_HURT);
    }

    @Override
    public boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, LivingEntity entity, boolean notApplicable) {
        if(notApplicable)return true;
        if(!(instance.getEffect() == MobEffects.BLINDNESS || instance.getEffect() == MobEffects.DARKNESS))return false;
        int levelmax = 0;
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack nwtool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            levelmax += nwtool.getModifierLevel(ModifierIds.sonanoka);
        }
        levelmax--;
        int nowlevel = -1;
        if(entity.hasEffect(MobeffectRegistry.SONANOKA.get()))
        {
            nowlevel = entity.getEffect(MobeffectRegistry.SONANOKA.get()).getAmplifier();
        }
        if(nowlevel >= levelmax)return false;
        entity.removeEffect(MobeffectRegistry.SONANOKA.get());
        entity.addEffect(new MobEffectInstance(MobeffectRegistry.SONANOKA.get(), 80 , nowlevel + 1,false, true));
        return true;
    }
}
