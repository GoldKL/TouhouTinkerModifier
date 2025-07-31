package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EldritchBlastSpell.class)
public class EldritchBlastSpellMixin {
    @WrapOperation(method = "onCast",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/damage/DamageSources;applyDamage(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)Z"),remap = false)
    boolean onCastmixin(Entity target, float damage, DamageSource adjustedDamage, Operation<Boolean> original)
    {
        if(target instanceof LivingEntity lventity)
        {
            lventity.invulnerableTime = 0;
        }
        return original.call(target, damage, adjustedDamage);
    }
}
