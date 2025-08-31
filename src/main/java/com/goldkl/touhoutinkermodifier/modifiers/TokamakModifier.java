package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TokamakModifier extends Modifier implements MeleeHitModifierHook, MeleeDamagePercentModifierHook{
    //托卡马克：灵乌路空
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        int level = modifier.getLevel() - 1;
        LivingEntity target = context.getLivingTarget();
        if(target != null){
            target.addEffect(new MobEffectInstance(MobeffectRegistry.RADIATION.get(), 40 + level * 20, level), context.getAttacker());
        }
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity target = context.getLivingTarget();
        if(target != null && target.hasEffect(MobeffectRegistry.RADIATION.get())){
            int level = modifier.getLevel();
            float percent;
            if(level <= 3)
            {
                percent = level * 1.1f;
            }
            else
            {
                percent = 3.3f + (level - 3) * 0.55f;
            }
            damagemodifier.addPercent(percent);
        }
    }
}
