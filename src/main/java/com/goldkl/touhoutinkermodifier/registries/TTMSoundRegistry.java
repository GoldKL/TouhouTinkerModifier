package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TTMSoundRegistry {
    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, TouhouTinkerModifier.MODID);
    public static final RegistryObject<SoundEvent>THE_WORLD_CAST = registerFixedSoundEvent("spell.theworld.cast",16);
    public static final RegistryObject<SoundEvent>THE_WORLD_FINISH = registerFixedSoundEvent("spell.theworld.finish",16);
    public static final RegistryObject<SoundEvent>MASTER_SPARK_TRUE_CAST = registerFixedSoundEvent("spell.master_spark.true_cast",16);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name)));
    }
    private static RegistryObject<SoundEvent> registerFixedSoundEvent(String name,float range) {
        return REGISTRY.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name),range));
    }
}
