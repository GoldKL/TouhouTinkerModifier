package com.goldkl.touhoutinkermodifier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TouhouTinkerModifierConfig {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL_STRINGS = BUILDER
                .comment("A list of dimension for HellcrowModifier.")
                .defineListAllowEmpty("dimension", List.of("minecraft:the_nether"), obj->true);
        static final ForgeConfigSpec SPEC = BUILDER.build();
        public static Set<ResourceKey<Level>> hellcrow_dimension;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event)
        {
            hellcrow_dimension = LEVEL_STRINGS.get().stream()
                    .map(level_name -> ResourceKey.create(Registries.DIMENSION,ResourceLocation.parse(level_name)))
                    .collect(Collectors.toSet());
        }

}
