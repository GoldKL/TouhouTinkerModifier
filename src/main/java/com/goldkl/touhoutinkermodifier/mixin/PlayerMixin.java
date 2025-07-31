package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getFlyingSpeed",at = @At("RETURN"), cancellable = true)
    void getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        AttributeInstance flyingSpeedAttribute = ((Player)(Object)this).getAttribute(AttributesRegistry.PLAYER_FLY_MOVEMENT.get());
        if (flyingSpeedAttribute != null) {
            double speed_modifier = flyingSpeedAttribute.getValue();
            cir.setReturnValue((float)speed_modifier * cir.getReturnValue());
        }
    }
}
