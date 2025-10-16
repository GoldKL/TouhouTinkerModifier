package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.api.LivingEntityInWorldEnder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel p_250460_, GameProfile p_249912_) {
        super(p_250460_, p_249912_);
    }
    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    boolean worlderenderfly(ItemStack instance, LivingEntity livingEntity, Operation<Boolean> original)
    {
        return original.call(instance,livingEntity)||((LivingEntityInWorldEnder)livingEntity).touhouTinkerModifier$isCurrentlyWorldender();
    }
}
