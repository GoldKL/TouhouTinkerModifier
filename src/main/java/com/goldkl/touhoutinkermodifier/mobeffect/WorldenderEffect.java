package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.FlyingToken;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID)
public class WorldenderEffect extends MobEffect {
    public WorldenderEffect() {
        super(MobEffectCategory.BENEFICIAL,0xFF0000);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "5787376E-2FE4-493D-8B41-F42E0AC9A542", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "5787376E-2FE4-493D-8B41-F42E0AC9A542", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(AttributesRegistry.PLAYER_FLY_MOVEMENT.get(), "5787376E-2FE4-493D-8B41-F42E0AC9A542", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ALObjects.Attributes.LIFE_STEAL.get(), "5787376E-2FE4-493D-8B41-F42E0AC9A542", 0.3, AttributeModifier.Operation.ADDITION);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(entity.level().isClientSide)return;
        if(entity instanceof Player player)
            FlyingToken.tickFlying(player);
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
    @Override
    public double getAttributeModifierValue(int level, AttributeModifier modifier) {
        if(level < 0) level = level & 0xFF;
        if(modifier.getAmount() == 0.15) return modifier.getAmount() + 0.15 * level;
        return modifier.getAmount() + 0.1 * level;
    }
    @SubscribeEvent
    static void OnLivingEntityHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.hasEffect(MobeffectRegistry.WORLDENDER.get()))
        {
            int level = entity.getEffect(MobeffectRegistry.WORLDENDER.get()).getAmplifier();
            float percent = 1.3f + 0.15f * level;
            event.setAmount(event.getAmount() * percent);
        }
    }
    @SubscribeEvent
    static void OnLivingEntityDeath(LivingDeathEvent event) {
        if(event.getSource().is(TagsRegistry.DamageTypeTag.PASS_WORLD_ENDER))return;
        LivingEntity entity = event.getEntity();
        if(entity.hasEffect(MobeffectRegistry.WORLDENDER.get()))
        {
            entity.removeEffect(MobeffectRegistry.WORLDENDER.get());
            entity.setHealth(entity.getMaxHealth() * 0.3f);
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            event.setCanceled(true);
        }
    }
}
