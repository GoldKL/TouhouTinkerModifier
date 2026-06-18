package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifierConfig;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class DimensionCoefficientEvent {
    public static double getEntityBoundaryPower(@NotNull Entity entity){
        if(entity instanceof LivingEntity lv){
            AttributeInstance instance = lv.getAttribute(AttributesRegistry.BOUNDARY_POWER.get());
            if(instance != null){
                return instance.getValue();
            }
        }
        return getLevelBoundaryPower(entity.level());
    }
    public static double getLevelBoundaryPower(@NotNull Level world){
        return TouhouTinkerModifierConfig.dimension_coefficient.getOrDefault(world.dimension(),0.0);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void DimensionCoefficientLivingHurt(LivingHurtEvent event) {
        if(event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))return;
        Entity attacker = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        double dimension_boundary_power = getLevelBoundaryPower(target.level());
        AttributeInstance instance_target = target.getAttribute(AttributesRegistry.BOUNDARY_POWER.get());
        AttributeInstance instance_attacker = null;
        if(attacker instanceof LivingEntity lv)
        {
            instance_attacker = lv.getAttribute(AttributesRegistry.BOUNDARY_POWER.get());
        }
        //目前玩家pvp之间不计算境界之力，只考虑玩家对非玩家造成伤害和非玩家对玩家造成伤害
        if(instance_attacker != null && instance_target == null)
        {
            //玩家造成伤害
            //造成的伤害*玩家维度系数/对方维度系数，上限为2，下限为0
            double attacker_boundary_power = instance_attacker.getValue();
            double coefficient = dimension_boundary_power != 0 ? Math.min(2,attacker_boundary_power / dimension_boundary_power) : 2;
            float true_coefficient = Math.round(coefficient * 10)/10f;
            if(attacker instanceof LivingEntity lv && TTMEntityUtils.hasModifier(lv, TTMModifierIds.tanabatazakamugennou))
            {
                event.setAmount(Math.max(event.getAmount() * true_coefficient, event.getAmount() * 0.1f));
            }
            else {
                event.setAmount(event.getAmount() * true_coefficient);
            }
        }
        else if(instance_attacker == null && instance_target != null)
        {
            //非玩家造成伤害
            //旧：上限为4，下限为0.25，线性
            //新：上限为10，下限为0.1，指数函数
            double target_boundary_power = instance_target.getValue();
            double coefficient = dimension_boundary_power != 0 ? Math.min(2,target_boundary_power / dimension_boundary_power) : 2;
            //float true_coefficient = Math.round((-coefficient * 4.95 + 10) * 10)/10f;
            float true_coefficient = (float) (Math.ceil(Math.pow(10,2-coefficient))/10.0);
            event.setAmount(event.getAmount() * true_coefficient);
            /*if(attacker instanceof LivingEntity lv && TTMEntityUtils.hasModifier(lv, TTMModifierIds.tanabatazakamugennou))
            {
                event.setAmount(Math.max(event.getAmount() * true_coefficient, event.getAmount() * 0.1f));
            }
            else {
                event.setAmount(event.getAmount() * true_coefficient);
            }*/
        }
    }
}
