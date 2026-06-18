package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.api.DamageModifierToolAttackContext;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.Stack;
import java.util.function.DoubleSupplier;


@Mixin(ToolAttackUtil.class)
public abstract class ToolAttackUtilMixin {
    @Shadow(remap = false)
    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack) {
        return false;
    }
    @WrapOperation(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Z"
            ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/tools/helper/ToolAttackUtil;performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"),remap = false)
    private static boolean attackEntitymixinbettercombat(IToolStackView tool, ToolAttackContext context, Operation<Boolean> original,@Local(argsOnly = true) Player attacker,@Local(argsOnly = true) Entity target)
    {
        AttackHand currentHand = ((EntityPlayer_BetterCombat)attacker).getCurrentAttack();
        if (currentHand != null) {
            ToolAttackContext.Builder builder = ToolAttackContext
                    .attacker(attacker)
                    .target(target)
                    .defaultCooldown()
                    .hand(currentHand.isOffHand()?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND);
            ToolStack truetool = currentHand.isOffHand()?ToolStack.from(attacker.getOffhandItem()):ToolStack.from(attacker.getMainHandItem());
            if(currentHand.isOffHand()) {
                builder.toolAttributes(truetool);
            }
            else {
                builder.applyAttributes();
            }
            return original.call(truetool, builder.build());
        }
        return original.call(tool,context);
    }
    @WrapOperation(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
    ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeDamageModifierHook;getMeleeDamage(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;FF)F"),
    remap = false)
    private static float attackEntitymixindamagedelta(MeleeDamageModifierHook instance, IToolStackView tool, ModifierEntry ingore, ToolAttackContext context, float basedamage, float damage, Operation<Float> original)
    {
        if(((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier() == null)
        {
            DamageModifier damageModifier = new DamageModifier(basedamage);
            List<ModifierEntry> modifiers = tool.getModifierList();
            for(ModifierEntry entry : modifiers) {
                damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, basedamage, damage);
            }
            float originDamagefix = damage - basedamage;
            damageModifier.addAdd(originDamagefix);
            for(ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeDamageModifier(tool, entry, context, basedamage, damage, damageModifier);
            }
            ((DamageModifierToolAttackContext)context).touhouTinkerModifier$setDamageModifier(damageModifier);
        }
        return ((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier().getamount();
    }
    @Inject(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At(value = "INVOKE",
                target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;getCriticalModifier()F"),remap = false)
    private static void CriticalModifier(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir)
    {
        ((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier().addMultiply(context.getCriticalModifier());
    }
    @Inject(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;getCooldown()F"),remap = false)
    private static void cooldownModifier(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir)
    {
        float cooldown = context.getCooldown();
        float cooldownfix = 1;
        if (cooldown < 1) {
            cooldownfix *= (0.2f + cooldown * cooldown * 0.8f);
        }
        ((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier().addMultiply(cooldownfix);
    }
    @Inject(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;getLivingTarget()Lnet/minecraft/world/entity/LivingEntity;"),remap = false)
    private static void fixdamage(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir, @Local(name = "damage") LocalFloatRef dmg)
    {
        dmg.set(((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier().getamount());
    }
    @WrapOperation(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At(value = "INVOKE",
                target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeHitModifierHook;afterMeleeHit(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;F)V"),remap = false)
    private static void aftermeleehitmixin(MeleeHitModifierHook instance, IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt, Operation<Void> original)
    {
        original.call(instance, tool, modifier, context, damageDealt);
        modifier.getHook(ModifierHooksRegistry.AFTER_MELEE_HIT).afterMeleeHitWithTheoryDamage(tool,modifier,context,((DamageModifierToolAttackContext)context).touhouTinkerModifier$getDamageModifier().getamount());
    }
}
