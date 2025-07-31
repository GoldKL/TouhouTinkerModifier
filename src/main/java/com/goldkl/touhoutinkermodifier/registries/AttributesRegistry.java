package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributesRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TouhouTinkerModifier.MODID);
    public static final RegistryObject<Attribute> MANA_COST_REDUCTION = ATTRIBUTES.register("mana_cost_reduction", () -> (new MagicRangedAttribute("attribute.touhoutinkermodifier.mana_cost_reduction", 1.0D, -100, 100.0D).setSyncable(true)));
    public static final RegistryObject<Attribute> PLAYER_FLY_MOVEMENT = ATTRIBUTES.register("player_fly_movement", () -> (new MagicRangedAttribute("attribute.touhoutinkermodifier.player_fly_movement", 1.0D, -100, 100.0D).setSyncable(true)));
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PLAYER_FLY_MOVEMENT.get());
        addToAll(event, MANA_COST_REDUCTION);
    }
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    private static void addToAll(EntityAttributeModificationEvent event, RegistryObject<Attribute> attribute, double defaultValue) {
        Attribute attr = attribute.get();
        for (EntityType<? extends LivingEntity> entity : event.getTypes()) {
            event.add(entity, attr, defaultValue);
        }
    }

    private static void addToAll(EntityAttributeModificationEvent event, RegistryObject<Attribute> attribute) {
        addToAll(event, attribute, attribute.get().getDefaultValue());
    }
}
