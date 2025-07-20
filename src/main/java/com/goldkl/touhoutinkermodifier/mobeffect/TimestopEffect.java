package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.api.LevelhaveLivingEntityTimeStop;
import com.goldkl.touhoutinkermodifier.api.LivingEntityCanStopTime;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.shared.TinkerEffects;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class TimestopEffect extends MobEffect {
    public TimestopEffect() {
        super(MobEffectCategory.BENEFICIAL,0xFFFF00);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(entity instanceof Player player) {
            player.causeFoodExhaustion(0.2F);// * (float) (amplifier + 1));
            if(player.getFoodData().getFoodLevel() == 0)
            {
                player.removeEffect(this);
            }
        }
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
    public static final double distance = 4.0;
    @SubscribeEvent
    static void onTeleport(EntityTeleportEvent event) {
        boolean flag = false;
        if (event.getEntity() instanceof LivingEntity living) {
            for(LivingEntity livingEntity:((LevelhaveLivingEntityTimeStop)(Object)living.level()).touhouTinkerModifier$getLivingEntitiesHavingTimeStop())
            {
                int level = ((LivingEntityCanStopTime)livingEntity).touhouTinkerModifier$isCanStopTime() + 1;
                if(livingEntity.distanceTo(living) <= level * TimestopEffect.distance)
                {
                    flag = true;
                    break;
                }
            }
        }
        if(flag)
        {
            event.setCanceled(true);
        }
    }
}
