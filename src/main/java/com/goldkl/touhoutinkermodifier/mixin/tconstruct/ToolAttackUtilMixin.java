package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Iterator;
import java.util.List;
import java.util.function.DoubleSupplier;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getCooldownFunction;

@Mixin(ToolAttackUtil.class)
public abstract class ToolAttackUtilMixin {
    @Shadow(remap = false)
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
    @Inject(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z"
            ,at = @At(value = "INVOKE"
                , target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D")
            ,locals = LocalCapture.CAPTURE_FAILHARD,remap = false)
    private static void attackEntitymixin(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir
                                          , @Local(name = "cooldown") LocalFloatRef cooldown, @Local(name = "isCritical") LocalBooleanRef isCritical, @Local(ordinal = 0) LocalRef<ToolAttackContext> context)
    {
        isCritical.set(context.get().isCritical());
        cooldown.set(context.get().getCooldown());
    }
}
