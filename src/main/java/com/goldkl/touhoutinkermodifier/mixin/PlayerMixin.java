package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.api.LivingEntityInWorldEnder;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    @Shadow
    private final Abilities abilities = new Abilities();
    @Shadow
    public abstract void startFallFlying();
    @Inject(method = "getFlyingSpeed",at = @At("RETURN"), cancellable = true)
    void getFlyingSpeedmixin(CallbackInfoReturnable<Float> cir) {
        if(this.abilities.flying && !this.isPassenger())
        {
            AttributeInstance flyingSpeedAttribute = ((Player)(Object)this).getAttribute(AttributesRegistry.PLAYER_FLY_MOVEMENT.get());
            if (flyingSpeedAttribute != null) {
                double speed_modifier = flyingSpeedAttribute.getValue();
                cir.setReturnValue((float)speed_modifier * cir.getReturnValue());
            }
        }
    }
    @Inject(method = "tryToStartFallFlying",at = @At("RETURN"), cancellable = true)
    void tryToStartFallFlyingmixin(CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue() && !this.onGround() && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION))
        {
            if(((LivingEntityInWorldEnder)this).touhouTinkerModifier$isCurrentlyWorldender())
            {
                this.startFallFlying();
                cir.setReturnValue(true);
            }
        }
    }
}
