package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.monster.Spider$SpiderTargetGoal")
public abstract class SpiderAttackGoalMixin<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public SpiderAttackGoalMixin(Mob p_26060_, Class<T> p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);
    }
    @Inject(method = "canUse",at = @At("RETURN"),cancellable = true)
    void canUseMixin(CallbackInfoReturnable<Boolean> cir)
    {
        float f = this.mob.getLightLevelDependentMagicValue();
        if(f < 0.5 && cir.getReturnValue())
        {
            if(this.target != null)
            {
                boolean flag = TTMEntityUtils.hasModifier(this.target, ModifierIds.tsuchigumo);
                cir.setReturnValue(flag);
            }
        }
    }
}
