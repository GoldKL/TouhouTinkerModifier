package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.DamageTypesRegistry;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL,0x00ff00);
    }
    @SubscribeEvent
    static void OnLivingEntityHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.hasEffect(MobeffectRegistry.RADIATION.get()))
        {
            event.setCanceled(true);
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if(amplifier == 0) return false;
        int k = 50 >> (amplifier - 1);
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(DamageTypesRegistry.source(entity.level().registryAccess(), DamageTypesRegistry.radiation_hurt), 1.0F);
    }
}
