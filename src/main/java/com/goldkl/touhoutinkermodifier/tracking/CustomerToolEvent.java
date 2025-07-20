package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.api.event.OndodgeEvent;
import com.goldkl.touhoutinkermodifier.api.event.PredodgeEvent;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.hook.EntityHealHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class CustomerToolEvent {
    @SubscribeEvent
    static void livingHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker)
        {
            float originalDamage = event.getAmount();
            float basedamage = originalDamage;
            DamageSource source = event.getSource();
            EquipmentContext context = new EquipmentContext(attacker);
            originalDamage = AttackerWithEquipmentModifyDamageModifierHook.attackermodifyDamageTaken(ModifierHooksRegistry.ATTACKER_MODIFY_HURT, context, source,basedamage, originalDamage, OnAttackedModifierHook.isDirectDamage(source));
            event.setAmount(originalDamage);
            if (originalDamage <= 0.0F) {
                event.setCanceled(true);
            }
        }

    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    static void livingDamage(LivingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker)
        {
            float originalDamage = event.getAmount();
            float basedamage = originalDamage;
            DamageSource source = event.getSource();
            EquipmentContext context = new EquipmentContext(attacker);
            originalDamage = AttackerWithEquipmentModifyDamageModifierHook.attackermodifyDamageTaken(ModifierHooksRegistry.ATTACKER_MODIFY_DAMAGE, context, source,basedamage , originalDamage, OnAttackedModifierHook.isDirectDamage(source));
            event.setAmount(originalDamage);
            if (originalDamage <= 0.0F) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    static void PreDodge(PredodgeEvent event) {
        if(event.getResult()!=Event.Result.DEFAULT)
            return;
        LivingEntity entity = event.getEntity();
        Entity attacker = event.getAttacker();
        Entity directattacker = event.getDirectattacker();
        EquipmentContext context = new EquipmentContext(entity);
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    if (entry.getHook(ModifierHooksRegistry.ENTITY_DODGE_HOOK).CanDodge(toolStack, entry, context, slotType, attacker, directattacker)) {
                        event.setResult(Event.Result.ALLOW);
                        return;
                    }
                }
            }
        }
    }
    @SubscribeEvent
    static void OnDodge(OndodgeEvent event) {
        LivingEntity entity = event.getEntity();
        Entity attacker = event.getAttacker();
        Entity directattacker = event.getDirectattacker();
        EquipmentContext context = new EquipmentContext(entity);
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    entry.getHook(ModifierHooksRegistry.ENTITY_DODGE_HOOK).OnDodge(toolStack, entry, context, slotType, attacker, directattacker);
                }
            }
        }
    }
    @SubscribeEvent
    static void OnLivingEntityHeall(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentContext context = new EquipmentContext(entity);
        float heal = EntityHealHook.modifyhealTaken(context,event.getAmount(),event.getAmount());
        event.setAmount(heal);
        if(heal <= 0.0F)
        {
            event.setCanceled(true);
        }
    }
}
