package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.DoubleSupplier;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getCooldownFunction;

@Mixin(ToolAttackUtil.class)
public abstract class ToolAttackUtilMixin {
    @Shadow
    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack) {
        return false;
    }

    @Inject(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Z",at = @At("HEAD"),cancellable = true,remap = false)
    private static void attackEntitymixin(IToolStackView tool, Player attacker, Entity targetEntity, CallbackInfoReturnable<Boolean> cir) {
        AttackHand currentHand = ((EntityPlayer_BetterCombat)attacker).getCurrentAttack();
        if (currentHand != null) {
            ToolStack truetool = currentHand.isOffHand()?ToolStack.from(attacker.getOffhandItem()):ToolStack.from(attacker.getMainHandItem());
            if(currentHand.isOffHand())
            {
                cir.setReturnValue(attackEntity(truetool, attacker, InteractionHand.OFF_HAND, targetEntity, getCooldownFunction(attacker, InteractionHand.OFF_HAND), false));
            }
            else
            {
                cir.setReturnValue(attackEntity(truetool, attacker, InteractionHand.MAIN_HAND, targetEntity, getCooldownFunction(attacker, InteractionHand.MAIN_HAND), false));
            }
        }
    }
}
