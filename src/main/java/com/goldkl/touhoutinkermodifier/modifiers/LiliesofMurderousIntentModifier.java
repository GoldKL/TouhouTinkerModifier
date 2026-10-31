package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagArrowModule;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class LiliesofMurderousIntentModifier extends Modifier implements ProjectileHitModifierHook {
    //杀意的百合：纯狐
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new AddDamageTypeTagArrowModule(LivingEntityPredicate.ANY, DamageTypeTags.BYPASSES_ARMOR,true));
        hookBuilder.addModule(new AddDamageTypeTagArrowModule(LivingEntityPredicate.ANY, DamageTypeTags.BYPASSES_EFFECTS,true));
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if(notBlocked && attacker != null && target != null && target.isDeadOrDying()){
            int level = modifier.getLevel();
            attacker.heal(attacker.getMaxHealth() * 0.03f * level);
        }
        return false;
    }
}
