package com.goldkl.touhoutinkermodifier.tracking;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.*;
import com.goldkl.touhoutinkermodifier.data.material.*;
import com.goldkl.touhoutinkermodifier.data.tags.TTMDamageTypesTagProvider;
import com.goldkl.touhoutinkermodifier.data.tags.TTMItemTagProvider;
import com.goldkl.touhoutinkermodifier.data.tags.TTMModifierTagProvider;
import com.goldkl.touhoutinkermodifier.module.CurioAttributeModule;
import com.goldkl.touhoutinkermodifier.utils.TTMPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;

import java.util.Set;
import java.util.concurrent.CompletableFuture;



@Mod.EventBusSubscriber(modid = TouhouTinkerModifier.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();

        boolean server = event.includeServer();
        boolean client = event.includeClient();


        DamageTypesProvider.register(registrySetBuilder);
        DatapackBuiltinEntriesProvider datapackRegistryProvider = new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, registrySetBuilder, Set.of(TouhouTinkerModifier.MODID));
        generator.addProvider(server, datapackRegistryProvider);
        generator.addProvider(server, new TTMRecipeProvider(packOutput));
        generator.addProvider(server, new TTMToolDefinitionDataProvider(packOutput));
        generator.addProvider(server, new TTMStationSlotLayoutProvider(packOutput));
        generator.addProvider(client, new TTMToolItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(client, new TTMItemModelProvider(packOutput, existingFileHelper));
        //material
        TTMMaterialDataProvider materials = new TTMMaterialDataProvider(packOutput);
        generator.addProvider(server, materials);
        generator.addProvider(server, new TTMMaterialStatsDataProvider(packOutput, materials));
        generator.addProvider(server, new TTMMaterialTraitsDataProvider(packOutput, materials));

        TTMTinkerMaterialSpriteProvider materialSprites = new TTMTinkerMaterialSpriteProvider();
        TTMTinkerPartSpriteProvider partSprites = new TTMTinkerPartSpriteProvider();
        generator.addProvider(client, new TTMMaterialRenderInfoProvider(packOutput, materialSprites, existingFileHelper));
        generator.addProvider(client, new GeneratorPartTextureJsonGenerator(packOutput, TouhouTinkerModifier.MODID, partSprites));
        generator.addProvider(client, new MaterialPartTextureGenerator(packOutput, existingFileHelper, partSprites, materialSprites));

        TTMBlockTagProvider blockTags = new TTMBlockTagProvider (packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new TTMItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(server, new TTMModifierTagProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(server, new ModifiersProvider(packOutput));
        generator.addProvider(server, new TTMDamageTypesTagProvider(packOutput, datapackRegistryProvider.getRegistryProvider(), existingFileHelper));

    }
    @SubscribeEvent
    public static void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            DamageSourcePredicate.LOADER.register(TouhouTinkerModifier.getResource("physical_damage"), TTMPredicate.PHYSICAL_DAMAGE.getLoader());
            DamageSourcePredicate.LOADER.register(TouhouTinkerModifier.getResource("not_ignore_damage"), TTMPredicate.NOT_IGNORE_DAMAGE.getLoader());
        }
    }
}
