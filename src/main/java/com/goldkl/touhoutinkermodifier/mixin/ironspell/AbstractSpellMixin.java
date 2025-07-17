package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.api.IAbstractSpell;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(AbstractSpell.class)
public abstract class AbstractSpellMixin implements IAbstractSpell {
    @Shadow(remap = false)
    public abstract int getManaCost(int level);
    @Unique
    @Override
    public int touhouTinkerModifier$getEntityManaCost(int level, @Nullable Entity sourceEntity)
    {
        double entityManaCostReduction = 1.0;
        if (sourceEntity instanceof LivingEntity livingEntity) {
            entityManaCostReduction = livingEntity.getAttributeValue(AttributesRegistry.MANA_COST_REDUCTION.get());
        }
        return (int) (getManaCost(level)*(2 - Utils.softCapFormula(entityManaCostReduction)));
    }
    @WrapOperation(method = "canBeCastedBy",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    public int canBeCastedBymixin(AbstractSpell instance, int level, Operation<Integer> original, @Local(argsOnly = true) Player player)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, player);
    }
    @WrapOperation(method = "castSpell",at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;getManaCost(I)I"),remap = false)
    public int castSpellmixin(AbstractSpell instance, int level, Operation<Integer> original, @Local(argsOnly = true) ServerPlayer serverPlayer)
    {
        return ((IAbstractSpell)(Object)instance).touhouTinkerModifier$getEntityManaCost(level, serverPlayer);
    }
}
