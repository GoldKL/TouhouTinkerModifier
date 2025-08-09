package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.entity.spell.lightning.SkysplitterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesRegistry {
    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TouhouTinkerModifier.MODID);
    public static final RegistryObject<EntityType<SkysplitterEntity>> SkySplitter =
            REGISTRY.register("sky_splitter", () -> EntityType.Builder.<SkysplitterEntity>of(SkysplitterEntity::new, MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(64)
                    .build((ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "sky_splitter")).toString()));
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
