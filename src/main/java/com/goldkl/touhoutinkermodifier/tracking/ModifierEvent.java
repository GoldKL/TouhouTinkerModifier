package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class ModifierEvent {
    //秋日澄空：秋静叶&&秋穰子
    //不会踩坏耕地
    @SubscribeEvent
    static void EntityFallonFarmBlock(BlockEvent.FarmlandTrampleEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity livingEntity)
        {
            if(TTMEntityUtils.hasModifier(livingEntity, TTMModifierIds.autumnsky)) {
                event.setCanceled(true);
            }
        }
    }
    //不会受到浆果丛的伤害
    @SubscribeEvent
    static void EntityAttackedBySweetBerryBush(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        if(event.getSource().is(DamageTypes.SWEET_BERRY_BUSH))
        {
            if(TTMEntityUtils.hasModifier(entity, TTMModifierIds.autumnsky)) {
                event.setCanceled(true);
            }
        }
    }
    //脱离战斗状态时间，每次攻击清0
    public final static ResourceLocation NotFightingTime = TouhouTinkerModifier.getResource("not_fighting_time");
    @SubscribeEvent
    public static void LivingEntityFightingTimeTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(NotFightingTime);
            if(time < 1000){
                data.putInt(NotFightingTime, time + 1);
            }
        });
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void LivingEntityFightingTimeOnAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.isAlive()){
            entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                data.putInt(NotFightingTime, 0);
            });
        }
        Entity source = event.getSource().getEntity();
        if(source instanceof LivingEntity entity1 && entity1.isAlive()){
            entity1.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                data.putInt(NotFightingTime, 0);
            });
        }
    }
}
