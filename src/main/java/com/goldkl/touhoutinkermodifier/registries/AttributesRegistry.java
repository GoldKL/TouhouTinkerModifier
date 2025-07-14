package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
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
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute.get())));
    }
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
