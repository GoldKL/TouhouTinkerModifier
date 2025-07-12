package com.goldkl.touhoutinkermodifier.modifiers;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;
import java.util.Optional;

public class CrimsonfantasyModifier extends Modifier implements ProjectileHitModifierHook {
    //深红幻想：蕾米莉亚
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if(attacker != null && target != null) {
            int level = modifier.getLevel();
            double entitySpellPowerModifier = 1;
            double entitySchoolPowerModifier = 1;
            float configPowerModifier = 1;
            float baseSpellPower = 10;
            float spellPowerPerLevel = 4;
            entitySpellPowerModifier = (float) attacker.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = (float) attacker.getAttributeValue(AttributeRegistry.BLOOD_SPELL_POWER.get());
            float basedamage = (float) ((baseSpellPower + spellPowerPerLevel * (level - 1)) * entitySpellPowerModifier * entitySchoolPowerModifier * configPowerModifier);
            float finaldamage = basedamage * DamageSources.getResist(target, SchoolRegistry.BLOOD.get());
            Optional<Holder.Reference<DamageType>> option = attacker.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DamageTypes.MAGIC);
            Holder<DamageType> holder = option.isPresent() ? (Holder) option.get() : attacker.level().damageSources().genericKill().typeHolder();
            target.hurt(new DamageSource(holder, attacker), finaldamage);
            target.invulnerableTime = 0;
        }
        return false;
    }
}
