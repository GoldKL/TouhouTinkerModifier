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

public class    UshinokokumairishichiModifier extends NoLevelsModifier implements MeleeDamageModifierHook {
    //丑时参拜第七日：水桥帕露西
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
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
                damage += rec.size() * 7;
                for(MobEffect effect : rec)
                {
                    entity.removeEffect(effect);
                }
            }
        }
        return damage;
    }
}
