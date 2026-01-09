package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
    /*@Unique
    private static boolean touhouTinkerModifier$Havedelatdamage = false;
    @Unique
    private static float touhouTinkerModifier$delatdamage = 0;*/
    @Unique
    private static ThreadLocal<Stack<Boolean>> touhouTinkerModifier$Havedelatdamage = new ThreadLocal<>();
    @Unique
    private static ThreadLocal<Stack<Float>> touhouTinkerModifier$delatdamage = new ThreadLocal<>();
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
    @Inject(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
    ,at = @At("HEAD"),remap = false)
    private static void attackEntitymixinhead(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir)
    {
        /*touhouTinkerModifier$Havedelatdamage = false;
        touhouTinkerModifier$delatdamage = 0;*/
        if(touhouTinkerModifier$Havedelatdamage.get() == null)
        {
            touhouTinkerModifier$Havedelatdamage.set(new Stack<>());
            touhouTinkerModifier$delatdamage.set(new Stack<>());
        }
        touhouTinkerModifier$Havedelatdamage.get().push(false);
        touhouTinkerModifier$delatdamage.get().push(0f);
    }
    @Inject(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At("RETURN"),remap = false)
    private static void attackEntitymixinreturn(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir)
    {
        /*touhouTinkerModifier$Havedelatdamage = false;
        touhouTinkerModifier$delatdamage = 0;*/
        touhouTinkerModifier$Havedelatdamage.get().pop();
        touhouTinkerModifier$delatdamage.get().pop();
        if(touhouTinkerModifier$Havedelatdamage.get().isEmpty())
        {
            touhouTinkerModifier$Havedelatdamage.remove();
            touhouTinkerModifier$delatdamage.remove();
        }
    }
    @WrapOperation(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
    ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeDamageModifierHook;getMeleeDamage(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;FF)F"),
    remap = false)
    private static float attackEntitymixindamagedelta(MeleeDamageModifierHook instance, IToolStackView tool, ModifierEntry ingore, ToolAttackContext context, float basedamage, float damage, Operation<Float> original)
    {
        /*if(!ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage)
        {
            MeleeDamagePercentModifierHook.DamageModifier damageModifier = new MeleeDamagePercentModifierHook.DamageModifier(basedamage);
            List<ModifierEntry> modifiers = tool.getModifierList();
            for(ModifierEntry entry : modifiers) {
                damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, basedamage, damage);
            }
            float originDamagefix = damage - basedamage;
            damageModifier.addAdd(originDamagefix);
            for(ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeDamageModifier(tool, entry, context, basedamage, damage, damageModifier);
            }
            ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage = true;
            ToolAttackUtilMixin.touhouTinkerModifier$delatdamage = damageModifier.getamount();
        }
        return ToolAttackUtilMixin.touhouTinkerModifier$delatdamage;*/
        if(!ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage.get().peek())
        {
            MeleeDamagePercentModifierHook.DamageModifier damageModifier = new MeleeDamagePercentModifierHook.DamageModifier(basedamage);
            List<ModifierEntry> modifiers = tool.getModifierList();
            for(ModifierEntry entry : modifiers) {
                damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, basedamage, damage);
            }
            float originDamagefix = damage - basedamage;
            damageModifier.addAdd(originDamagefix);
            for(ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeDamageModifier(tool, entry, context, basedamage, damage, damageModifier);
            }
            ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage.get().pop();
            ToolAttackUtilMixin.touhouTinkerModifier$delatdamage.get().pop();
            ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage.get().push(true);
            ToolAttackUtilMixin.touhouTinkerModifier$delatdamage.get().push(damageModifier.getamount());
        }
        return ToolAttackUtilMixin.touhouTinkerModifier$delatdamage.get().peek();
    }
    @WrapOperation(method = "performAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;)Z"
            ,at = @At(value = "INVOKE",
                target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeHitModifierHook;afterMeleeHit(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;F)V"),remap = false)
    private static void aftermeleehitmixin(MeleeHitModifierHook instance, IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt, Operation<Void> original)
    {
        original.call(instance, tool, modifier, context, damageDealt);
        //modifier.getHook(ModifierHooksRegistry.AFTER_MELEE_HIT).afterMeleeHitWithTheoryDamage(tool,modifier,context,ToolAttackUtilMixin.touhouTinkerModifier$delatdamage);
        modifier.getHook(ModifierHooksRegistry.AFTER_MELEE_HIT).afterMeleeHitWithTheoryDamage(tool,modifier,context,ToolAttackUtilMixin.touhouTinkerModifier$delatdamage.get().peek());
    }
}
