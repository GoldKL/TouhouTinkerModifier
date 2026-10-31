package com.goldkl.touhoutinkermodifier.mobeffect;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.api.LivingEntityInWorldEnder;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import io.redspace.ironsspellbooks.effect.IMobEffectEndCallback;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaximumdosageEffect extends MagicMobEffect implements ISyncedMobEffect {
    public static ResourceLocation NAME = TouhouTinkerModifier.getResource("maximumdosage");

    public static UUID uuid = UUID.nameUUIDFromBytes(NAME.toString().getBytes());
    public MaximumdosageEffect() {
        super(MobEffectCategory.BENEFICIAL,0x00ff00);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid.toString(), 0.10, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
    {
        if(!entity.level().isClientSide)
        {
            double maxHealth = entity.getMaxHealth();
            AttributeInstance instance = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if(instance != null){
                instance.removeModifier(uuid);
                instance.addPermanentModifier(new AttributeModifier(uuid,this::getDescriptionId,(0.2f + 0.1f * amplifier) * maxHealth,AttributeModifier.Operation.ADDITION));
            }
            entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent( data ->{
                int num = data.getInt(NAME);
                float maxhealth = entity.getMaxHealth();
                entity.heal(maxhealth * (0.05f + 0.025f * amplifier + 0.025f * num));
            });
        }
    }
    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        AttributeInstance instance = pLivingEntity.getAttribute(Attributes.MAX_HEALTH);
        if(instance != null){
            instance.removeModifier(uuid);
        }
        instance = pLivingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(instance != null){
            instance.removeModifier(uuid);
        }
        pLivingEntity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data ->{
            data.remove(NAME);
            TinkerNetwork.getInstance().sendToTrackingAndSelf(new SyncPersistentDataPacket(data.getCopy()), pLivingEntity);
        });
    }
    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent( data ->{
            int num = 0;
            if(pAmplifier >= 2)
                num = pLivingEntity.level().getEntitiesOfClass(LivingEntity.class, pLivingEntity.getBoundingBox().inflate(6)).size() - 1;
            num = Math.max(0,num);
            data.putInt(NAME,num);

            AttributeInstance instance = pLivingEntity.getAttribute(Attributes.MAX_HEALTH);
            if(instance != null){
                instance.removeModifier(uuid);
                instance.addPermanentModifier(new AttributeModifier(uuid,this::getDescriptionId,0.3f + 0.1f * pAmplifier + 0.1f * num,AttributeModifier.Operation.MULTIPLY_TOTAL));
            }

            float maxHealth = pLivingEntity.getMaxHealth();
            float damageHealth = maxHealth - pLivingEntity.getHealth();
            pLivingEntity.heal(damageHealth*(0.2f + 0.1f * pAmplifier + 0.1f * num));

            instance = pLivingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
            if(instance != null){
                instance.removeModifier(uuid);
                instance.addPermanentModifier(new AttributeModifier(uuid,this::getDescriptionId,(0.2f + 0.1f * pAmplifier) * maxHealth,AttributeModifier.Operation.ADDITION));
            }

            TinkerNetwork.getInstance().sendToTrackingAndSelf(new SyncPersistentDataPacket(data.getCopy()), pLivingEntity);

            });
    }
}
