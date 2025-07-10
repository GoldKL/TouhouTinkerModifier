package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.mixininterface.ToolAttackContextMixinInterface;
import org.spongepowered.asm.mixin.*;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(ToolAttackContext.class)
public class ToolAttackContextMixin implements ToolAttackContextMixinInterface {
    @Mutable
    @Final
    @Shadow(remap = false)
    private boolean isCritical;
    @Mutable
    @Final
    @Shadow(remap = false)
    private float cooldown;
    @Unique
    @Override
    public void touhouTinkerModifier$setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
    @Unique
    @Override
    public void touhouTinkerModifier$setCritical(boolean critical) {
        this.isCritical = critical;
    }
}
