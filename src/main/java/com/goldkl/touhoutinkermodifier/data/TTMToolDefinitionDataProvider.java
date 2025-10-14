package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TTMToolDefinitions;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ToolActions;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.definition.module.ToolModule;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.SetStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolActionsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedModifierModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static slimeknights.tconstruct.tools.TinkerToolParts.*;

public class TTMToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    public TTMToolDefinitionDataProvider(PackOutput packOutput) {
        super(packOutput, TouhouTinkerModifier.MODID);
    }
    @Override
    protected void addToolDefinitions() {
        RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
        RandomMaterial randomMaterial = RandomMaterial.random().allowHidden().build();
        DefaultMaterialsModule defaultTwoParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material).build();
        DefaultMaterialsModule defaultThreeParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material).build();
        DefaultMaterialsModule defaultFourParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material, tier1Material).build();
        DefaultMaterialsModule defaultFiveParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material, tier1Material, tier1Material).build();
        DefaultMaterialsModule ancientTwoParts = DefaultMaterialsModule.builder().material(randomMaterial, randomMaterial).build();
        DefaultMaterialsModule ancientThreeParts = DefaultMaterialsModule.builder().material(randomMaterial, randomMaterial, randomMaterial).build();
        ToolModule[] swordHarvest = {
                IsEffectiveModule.tag(TinkerTags.Blocks.MINABLE_WITH_SWORD),
                MiningSpeedModifierModule.blocks(7.5f, Blocks.COBWEB)
        };
        define(TTMToolDefinitions.SPEAR)
                // parts
                .module(PartStatsModule.parts()
                        .part(smallBlade)
                        .part(toolHandle, 0.5f)
                        .part(toolHandle, 0.5f)
                        .part(toolBinding).build())
                .module(defaultFourParts)
                // stats
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 3.2f)
                        .set(ToolStats.ATTACK_SPEED, 1.2f).build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 1.25f)
                        .set(ToolStats.MINING_SPEED, 0.5f)
                        .set(ToolStats.DURABILITY, 2.3f).build()))
                .smallToolStartingSlots()
                // traits
                .module(ToolTraitsModule.builder().trait(ModifierIds.pierce, 1).build())
                .module(ToolTraitsModule.builder().trait(TTMModifierIds.longspear,1).build())
                // behavior
                .module(ToolActionsModule.of(ToolActions.SWORD_DIG))
                .module(swordHarvest);
        define(TTMToolDefinitions.BRASS_KNUCKLES)
                // parts
                .module(PartStatsModule.parts()
                        .part(smallBlade,0.3f)
                        .part(smallBlade,0.3f)
                        .part(smallBlade,0.3f)
                        .part(smallBlade,0.3f)
                        .part(TinkerToolParts.plating.get(ArmorItem.Type.HELMET)).build())
                .module(defaultFiveParts)
                // stats
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 3f)
                        .set(ToolStats.ATTACK_SPEED, 3f)
                        .set(ToolStats.BLOCK_AMOUNT, 20)
                        .set(ToolStats.USE_ITEM_SPEED, 1.0f).build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 0.5f)
                        .set(ToolStats.MINING_SPEED, 1.5f)
                        .set(ToolStats.DURABILITY, 0.75f).build()))
                .smallToolStartingSlots();
        define(TTMToolDefinitions.GOHEI)
                // parts
                .module(PartStatsModule.parts()
                        .part(smallBlade)
                        .part(ItemsRegistry.GoheiCore)
                        .part(toughHandle)
                        .build())
                .module(defaultThreeParts)
                // stats
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 3f)
                        .set(ToolStats.USE_ITEM_SPEED,1.0f)
                        .set(ToolStats.ATTACK_SPEED, 1.6f).build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.MINING_SPEED, 0.5f)
                        .set(ToolStats.DURABILITY, 3f).build()))
                .smallToolStartingSlots()
                // behavior
                .module(ToolActionsModule.of(ToolActions.SWORD_DIG))
                .module(swordHarvest);
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier' Construct Tool Definition Data Generator";
    }
}
