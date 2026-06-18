package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.api.DamageModifierDamageSource;
import com.goldkl.touhoutinkermodifier.api.DamageModifierToolAttackContext;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(value = ToolAttackContext.class,priority = 9999)
public class ToolAttackContextMixin implements DamageModifierToolAttackContext {
    @Unique
    private DamageModifier touhouTinkerModifier$damageModifier = null;

    @Unique
    @Override
    public DamageModifier touhouTinkerModifier$getDamageModifier() {
        return touhouTinkerModifier$damageModifier;
    }

    @Unique
    @Override
    public void touhouTinkerModifier$setDamageModifier(DamageModifier damage) {
        touhouTinkerModifier$damageModifier = new DamageModifier(damage);
    }
    @WrapMethod(method = "makeDamageSource", remap = false)
    public DamageSource modifyDamageSource(Operation<DamageSource> original) {
        DamageSource source = original.call();
        if (this.touhouTinkerModifier$damageModifier != null) {
            ((DamageModifierDamageSource)source).touhouTinkerModifier$setDamageModifier(touhouTinkerModifier$damageModifier);
        }
        return source;
    }
}
