package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class FanghuaxuanlanModifier extends Modifier implements ProjectileHitModifierHook {
    //芳华绚烂：红美铃
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.PROJECTILE_HIT);
    }
    private static final int EFFECT_DURATION = 200;
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target)
    {
        int level = modifier.getLevel();
        if(target != null)
        {
            int Effectlevel = -1;
            if(target.hasEffect(MobeffectRegistry.INTERNALINJURY.get()))
            {
                Effectlevel = target.getEffect(MobeffectRegistry.INTERNALINJURY.get()).getAmplifier();
                target.removeEffect(MobeffectRegistry.INTERNALINJURY.get());
            }
            int neweffectlevel = Math.max(level + 2 , Effectlevel + 1);
            target.addEffect(new MobEffectInstance(MobeffectRegistry.INTERNALINJURY.get(),EFFECT_DURATION,neweffectlevel,false,true),attacker);
        }
        return false;
    }
}
