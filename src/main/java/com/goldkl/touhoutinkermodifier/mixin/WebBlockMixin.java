package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public class WebBlockMixin {
    @Inject(method = "entityInside",at = @At("HEAD"), cancellable = true)
    public void entityInsidemixin(BlockState block, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(entity instanceof LivingEntity livingEntity) {
            if(TTMEntityUtils.hasModifier(livingEntity, TTMModifierIds.tsuchigumo))
            {
                entity.resetFallDistance();
                ci.cancel();
            }
        }
    }
}
