package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
}
