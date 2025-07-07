package com.goldkl.touhoutinkermodifier.modifiers;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class    UshinokokumairishichiModifier extends NoLevelsModifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity entity = context.getLivingTarget();
        int count = 0;
        if(entity != null)
        {
            for(MobEffectInstance effect : entity.getActiveEffects())
            {
                if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                {
                    count++;
                }
            }
            if(count >= 7)
            {
                damage += count * 7;
            }
        }
        return damage;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        LivingEntity entity = context.getLivingTarget();
        if(entity != null)
        {
            List<MobEffect> rec = new ArrayList<>();
            for(MobEffectInstance effect : entity.getActiveEffects())
            {
                if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                {
                    rec.add(effect.getEffect());
                }
            }
            if(rec.size() >= 7)
            {
                for(MobEffect effect : rec)
                {
                    entity.removeEffect(effect);
                }
            }
        }
    }
}
