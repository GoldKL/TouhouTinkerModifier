package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.api.DamageModifierDamageSource;
import com.goldkl.touhoutinkermodifier.api.event.OndodgeEvent;
import com.goldkl.touhoutinkermodifier.api.event.PredodgeEvent;
import com.goldkl.touhoutinkermodifier.capability.TheKindofKillDataCapability;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.hook.AfterAttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.hook.EntityHealHook;
import com.goldkl.touhoutinkermodifier.mixin.ironspell.SpellSelectionManagerAccessor;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.network.EquipmentChangedPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class CustomerToolEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void livingDeath(LivingDeathEvent event) {
        if(event.isCanceled())return;
        LivingEntity target = event.getEntity();
        if(target.level().isClientSide())return;
        Entity attacker = event.getSource().getEntity();
        if(attacker instanceof Player player) {
            player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(data ->{
                    data.addEntity(target);
                }
            );
            TheKindofKillDataCapability.sync(player);
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void livingHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (entity instanceof LivingEntity attacker)
        {
            float basedamage = event.getAmount();
            DamageSource source = event.getSource();
            EquipmentContext context = new EquipmentContext(attacker);
            DamageModifier damageModifier;
            float fix = 1;
            //防止重复增伤
            if(((DamageModifierDamageSource)source).touhouTinkerModifier$getDamageModifier() == null){
                damageModifier = new DamageModifier(basedamage);
            } else {
                damageModifier = new DamageModifier(((DamageModifierDamageSource)source).touhouTinkerModifier$getDamageModifier());
                fix = basedamage/damageModifier.getamount();
                basedamage = damageModifier.getBaseamount();
            }
            AttackerWithEquipmentModifyDamageModifierHook.attackermodifyDamageTaken(ModifierHooksRegistry.ATTACKER_MODIFY_HURT,target, context, source, basedamage , damageModifier, OnAttackedModifierHook.isDirectDamage(source));
            event.setAmount(damageModifier.getamount() * fix);
            if (damageModifier.getamount()* fix <= 0.0F) {
                event.setCanceled(true);
            }
            else
            {
                AfterAttackerWithEquipmentModifyDamageModifierHook.afterattackermodifyDamageTaken(ModifierHooksRegistry.AFTER_ATTACKER_MODIFY_HURT,context,source,damageModifier.getamount(),OnAttackedModifierHook.isDirectDamage(source));
            }
        }

    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void livingDamage(LivingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (entity instanceof LivingEntity attacker)
        {
            float basedamage = event.getAmount();
            DamageSource source = event.getSource();
            EquipmentContext context = new EquipmentContext(attacker);
            DamageModifier damageModifier = new DamageModifier(basedamage);
            AttackerWithEquipmentModifyDamageModifierHook.attackermodifyDamageTaken(ModifierHooksRegistry.ATTACKER_MODIFY_DAMAGE,target, context, source,basedamage , damageModifier, OnAttackedModifierHook.isDirectDamage(source));
            event.setAmount(damageModifier.getamount());
            if (damageModifier.getamount() <= 0.0F) {
                event.setCanceled(true);
            }
            else
            {
                AfterAttackerWithEquipmentModifyDamageModifierHook.afterattackermodifyDamageTaken(ModifierHooksRegistry.AFTER_ATTACKER_MODIFY_DAMAGE,context,source,damageModifier.getamount(),OnAttackedModifierHook.isDirectDamage(source));
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
            IToolStackView toolStack = context.getValidTool(slotType);
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
            IToolStackView toolStack = context.getValidTool(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    entry.getHook(ModifierHooksRegistry.ENTITY_DODGE_HOOK).OnDodge(toolStack, entry, context, slotType, attacker, directattacker);
                }
            }
        }
    }
    @SubscribeEvent
    static void OnLivingEntityHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentContext context = new EquipmentContext(entity);
        float heal = EntityHealHook.modifyhealTaken(context,event.getAmount(),event.getAmount());
        event.setAmount(heal);
        if(heal <= 0.0F)
        {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void EffectApply(MobEffectEvent.Added event){
        if (event.getEntity()!=null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(event.getEntity().getItemBySlot(slot));
                if(tool != null && !tool.isBroken() && ModifierUtil.validArmorSlot(tool, slot)){
                    for (ModifierEntry entry:tool.getModifierList()){
                        entry.getHook(ModifierHooksRegistry.ENTITY_EFFECT_APPLICABLE_HURT).onApplicable(tool,entry,slot,event.getEffectInstance(),event.getEntity());
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void AddPlayerMagic(SpellSelectionManager.SpellSelectionEvent event){
        Player player = event.getEntity();
        EquipmentContext context = new EquipmentContext(player);
        SpellSelectionManager manager = event.getManager();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            IToolStackView tool = context.getValidTool(slot);
            if (tool != null && !tool.isBroken()) {
                ItemStack stack = player.getItemBySlot(slot);
                ArrayList<SpellData>list = new ArrayList<>();
                for (ModifierEntry entry : tool.getModifierList()){
                    ArrayList<SpellData>newlist = entry.getHook(ModifierHooksRegistry.ADD_PLAYER_MAGIC).getmagiclist(tool,entry,context,slot,manager);
                    if(newlist != null)list.addAll(newlist);
                }
                int num = ISpellContainer.isSpellContainer(stack) ? ISpellContainer.get(stack).getActiveSpellCount() : 0;
                for(SpellData spellData : list){
                    SpellSelectionManager.SelectionOption newoption = new SpellSelectionManager.SelectionOption(spellData, slot.getName(), num, manager.getSpellCount());
                    List<SpellSelectionManager.SelectionOption> options = manager.getAllSpells();
                    SpellSelectionManager.SelectionOption exist = null;
                    for (SpellSelectionManager.SelectionOption selectionOption : options) {
                        if (selectionOption.spellData.getSpell().equals(spellData.getSpell())) {
                            exist = selectionOption;
                            break;
                        }
                    }
                    if(exist != null){
                        if (newoption.spellData.getLevel() > exist.spellData.getLevel()) {
                            newoption.globalIndex = exist.globalIndex;
                            options.set(exist.globalIndex, newoption);
                        }
                    }
                    else {
                        options.add(newoption);
                    }
                    if (manager.getCurrentSelection().index == num && manager.getCurrentSelection().equipmentSlot.equals(slot.getName())) {
                        ((SpellSelectionManagerAccessor)manager).setSelectionIndex(manager.getSpellCount() - 1);
                        ((SpellSelectionManagerAccessor)manager).setSelectionValid(true);
                    }
                    num++;
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingEquipmentChangeEventMagic(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            boolean needupdate = false;
            for (ItemStack stack : new ItemStack[]{event.getFrom(), event.getTo()}) {
                if (TTMItemUtils.getToolStackIfModifiable(stack) != null) {
                    needupdate = true;
                    break;
                }
            }
            if(needupdate)
                PacketDistributor.sendToPlayer(serverPlayer, new EquipmentChangedPacket());
        }
    }
    @SubscribeEvent
    public static void ModifySpellLevel(ModifySpellLevelEvent event) {
        if (event.getEntity() != null) {
            LivingEntity entity = event.getEntity();
            EquipmentContext context = new EquipmentContext(entity);
            int baselevel = event.getBaseLevel();
            int totallevel = event.getLevel();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                IToolStackView tool = context.getValidTool(slot);
                if(tool != null && !tool.isBroken())
                {
                    AbstractSpell spell = event.getSpell();
                    for (ModifierEntry entry : tool.getModifierList()) {
                        totallevel = entry.getHook(ModifierHooksRegistry.MAGIC_AFFINITY).getMagicAffinity(tool,entry,context,slot,spell,baselevel,totallevel);
                    }
                }
            }
            event.setLevel(totallevel);
        }
    }
}
