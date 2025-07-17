package com.goldkl.touhoutinkermodifier.mixin.attributeslib;

import com.goldkl.touhoutinkermodifier.api.event.OndodgeEvent;
import com.goldkl.touhoutinkermodifier.api.event.PredodgeEvent;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowsoffire.attributeslib.impl.AttributeEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttributeEvents.class)
public class AttributeEventsMixin {
    @WrapOperation(method = "dodge(Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V"
            ,at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/attributeslib/impl/AttributeEvents;isDodging(Lnet/minecraft/world/entity/LivingEntity;)Z"),remap = false)
    private boolean LivingAttackEventisDodgingMixin (LivingEntity target, Operation<Boolean> original, @Local(argsOnly = true) LivingAttackEvent event)
    {
        Entity attacker = event.getSource().getEntity();
        Entity directattacker = event.getSource().getDirectEntity();
        return touhouTinkerModifier$isDodgingMixin(target, original, attacker, directattacker);
    }
    @WrapOperation(method = "dodge(Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V"
        ,at = @At(value = "INVOKE",target = "Ldev/shadowsoffire/attributeslib/impl/AttributeEvents;onDodge(Lnet/minecraft/world/entity/LivingEntity;)V"),remap = false)
    private void LivingAttackEventonDodgeMixin(AttributeEvents instance, LivingEntity target, Operation<Void> original, @Local(argsOnly = true) LivingAttackEvent event)
    {
        Entity attacker = event.getSource().getEntity();
        Entity directattacker = event.getSource().getDirectEntity();
        MinecraftForge.EVENT_BUS.post(new OndodgeEvent(target, attacker, directattacker));
        original.call(instance, target);
    }
    @WrapOperation(method = "dodge(Lnet/minecraftforge/event/entity/ProjectileImpactEvent;)V"
            ,at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/attributeslib/impl/AttributeEvents;isDodging(Lnet/minecraft/world/entity/LivingEntity;)Z"),remap = false)
    private boolean ProjectileImpactEventisDodgingMixin (LivingEntity target, Operation<Boolean> original, @Local(argsOnly = true) ProjectileImpactEvent event)
    {
        Projectile directattacker = event.getProjectile();
        Entity attacker = directattacker.getOwner();
        return touhouTinkerModifier$isDodgingMixin(target, original, attacker, directattacker);
    }
    @WrapOperation(method = "dodge(Lnet/minecraftforge/event/entity/ProjectileImpactEvent;)V"
            ,at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/attributeslib/impl/AttributeEvents;onDodge(Lnet/minecraft/world/entity/LivingEntity;)V"),remap = false)
    private void ProjectileImpactEventonDodgeMixin (AttributeEvents instance, LivingEntity target, Operation<Void> original, @Local(argsOnly = true) ProjectileImpactEvent event)
    {
        Projectile directattacker = event.getProjectile();
        Entity attacker = directattacker.getOwner();
        MinecraftForge.EVENT_BUS.post(new OndodgeEvent(target, attacker, directattacker));
        original.call(instance, target);
    }
    @Unique
    private static boolean touhouTinkerModifier$isDodgingMixin(LivingEntity target, Operation<Boolean> original, Entity attacker, Entity directattacker) {
        PredodgeEvent predodgeEvent = new PredodgeEvent(target, attacker, directattacker);
        MinecraftForge.EVENT_BUS.post(predodgeEvent);
        return switch (predodgeEvent.getResult()) {
            case DENY -> false;
            case ALLOW -> true;
            default -> original.call(target);
        };
    }
}
