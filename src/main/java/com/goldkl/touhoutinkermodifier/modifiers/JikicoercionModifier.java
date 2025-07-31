package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class JikicoercionModifier extends Modifier implements ProjectileHitModifierHook, MeleeHitModifierHook {
    //自机威压：自机组
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.MELEE_HIT);
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if(target != null) {
            applyEffect(target, modifier.getLevel());
        }
        return false;
    }
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if(context.getLivingTarget() != null) {
            applyEffect(context.getLivingTarget(), modifier.getLevel());
        }
        return knockback;
    }
    private void applyEffect(LivingEntity entity,int level)
    {
        int amplifier = -1;
        MobEffectInstance instance = entity.getEffect(MobeffectRegistry.MELT.get());
        if(instance != null)
        {
            amplifier = instance.getAmplifier();
            entity.removeEffect(MobeffectRegistry.MELT.get());
        }
        entity.addEffect(new MobEffectInstance(MobeffectRegistry.MELT.get(),100 * level, Math.min(2,amplifier + 1)));
    }
}
