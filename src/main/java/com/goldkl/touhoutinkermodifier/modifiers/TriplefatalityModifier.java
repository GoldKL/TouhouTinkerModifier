package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.AfterMeleeHitModifierHook;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TriplefatalityModifier extends Modifier implements MeleeDamagePercentModifierHook, AfterMeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT, ModifierHooksRegistry.AFTER_MELEE_HIT);
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity attacker = context.getAttacker();
        if(attacker.hasEffect(MobeffectRegistry.TRIPLEFATALITY.get())){
            MobEffectInstance instance = attacker.getEffect(MobeffectRegistry.TRIPLEFATALITY.get());
            assert instance != null;
            int level = instance.getAmplifier() + 1;
            damagemodifier.addMultiply((float) Math.pow(3,level));
        }
    }
    @Override
    public void afterMeleeHitWithTheoryDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt){
        LivingEntity attacker = context.getAttacker();
        if(!attacker.hasEffect(MobeffectRegistry.EXHAUSTED.get())){
            LivingEntity target = context.getLivingTarget();
            if(target != null){
                MobEffectInstance instance = attacker.getEffect(MobeffectRegistry.TRIPLEFATALITY.get());
                int level = (instance != null ? instance.getAmplifier() :-1) + 1;
                if(level == 2)
                {
                    attacker.removeEffect(MobeffectRegistry.TRIPLEFATALITY.get());
                    if(!target.isDeadOrDying())
                    {
                        attacker.addEffect(new MobEffectInstance(MobeffectRegistry.EXHAUSTED.get(), 3600, 3));
                    }
                    else
                    {
                        attacker.addEffect(new MobEffectInstance(MobeffectRegistry.EXHAUSTED.get(), 200, 0));
                    }
                }
                else
                {
                    attacker.addEffect(new MobEffectInstance(MobeffectRegistry.TRIPLEFATALITY.get(), 1200, level));
                }
            }
        }
    }
}
