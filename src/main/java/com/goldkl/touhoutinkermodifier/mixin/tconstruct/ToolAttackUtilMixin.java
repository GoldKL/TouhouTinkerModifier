package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.function.DoubleSupplier;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getCooldownFunction;

@Mixin(ToolAttackUtil.class)
public abstract class ToolAttackUtilMixin {
    @Shadow(remap = false)
    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack) {
        return false;
    }
    @Unique
    private static boolean touhouTinkerModifier$Havedelatdamage = false;
    @Unique
    private static float touhouTinkerModifier$delatdamage = 0;

    @Inject(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Z",at = @At("HEAD"),cancellable = true,remap = false)
    private static void attackEntitymixinbettercombat(IToolStackView tool, Player attacker, Entity targetEntity, CallbackInfoReturnable<Boolean> cir) {
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
    ,at = @At("HEAD"),remap = false)
    private static void attackEntitymixinhead(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, CallbackInfoReturnable<Boolean> cir)
    {
        touhouTinkerModifier$Havedelatdamage = false;
        touhouTinkerModifier$delatdamage = 0;
    }
    @WrapOperation(method = "attackEntity(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;Ljava/util/function/DoubleSupplier;ZLnet/minecraft/world/entity/EquipmentSlot;)Z"
    ,at = @At(value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/modifiers/hook/combat/MeleeDamageModifierHook;getMeleeDamage(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lslimeknights/tconstruct/library/modifiers/ModifierEntry;Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;FF)F"),
    remap = false)
    private static float attackEntitymixindamagedelta(MeleeDamageModifierHook instance, IToolStackView tool, ModifierEntry ingore, ToolAttackContext context, float basedamage, float damage, Operation<Float> original)
    {
        if(!ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage)
        {
            float[] modifierNum = new float[5];
            modifierNum[0] = 0;
            modifierNum[1] = 0;
            modifierNum[2] = 0;
            modifierNum[3] = 1.0f;
            modifierNum[4] = 0;
            List<ModifierEntry> modifiers = tool.getModifierList();
            for(ModifierEntry entry : modifiers) {
                damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, basedamage, damage);
            }
            modifierNum[0] = damage - basedamage;
            for(ModifierEntry entry : modifiers) {
                modifierNum[1] = entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeAdd(tool, entry, context, basedamage, damage ,modifierNum[1]);
            }
            for(ModifierEntry entry : modifiers) {
                modifierNum[2] = entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleePercent(tool, entry, context, basedamage, damage ,modifierNum[2]);
            }
            for(ModifierEntry entry : modifiers) {
                modifierNum[3] *= entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeMultiply(tool, entry, context, basedamage , damage);
            }
            for(ModifierEntry entry : modifiers) {
                modifierNum[4] = entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeFixed(tool, entry, context, basedamage , damage,modifierNum[4]);
            }
            ToolAttackUtilMixin.touhouTinkerModifier$Havedelatdamage = true;
            ToolAttackUtilMixin.touhouTinkerModifier$delatdamage = (basedamage + modifierNum[0] + modifierNum[1]) * (1 + modifierNum[2]) * modifierNum[3] + modifierNum[4];
        }
        return ToolAttackUtilMixin.touhouTinkerModifier$delatdamage;
    }
}
