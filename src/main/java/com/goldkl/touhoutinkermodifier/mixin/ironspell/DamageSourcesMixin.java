package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.helper.SpellDamageSourceInterface;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {
    @WrapOperation(method = "applyDamage",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/damage/DamageSources;getResist(Lnet/minecraft/world/entity/LivingEntity;Lio/redspace/ironsspellbooks/api/spells/SchoolType;)F"),remap = false)
    private static float applyDamagemixin(LivingEntity entity, SchoolType damageSchool, Operation<Float> original,@Local(argsOnly = true) DamageSource damageSource)
    {
        if(damageSource instanceof SpellDamageSource spellDamageSource){
            float original_resist = original.call(entity,damageSchool);
            if(original_resist < 1 && original_resist > 0){
                original_resist = 1.0f/original_resist - 1;
                original_resist *= 1 - ((SpellDamageSourceInterface) spellDamageSource).touhouTinkerModifier$getPassByResist();
                original_resist = 1.0f/(1+original_resist);
            }
            return original_resist;
        }
        return original.call(entity,damageSchool);
    }
}
