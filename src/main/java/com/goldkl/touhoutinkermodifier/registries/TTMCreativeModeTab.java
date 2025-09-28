package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

public class TTMCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TouhouTinkerModifier.MODID);

    public static final RegistryObject<CreativeModeTab> tabGeneral = CREATIVE_MODE_TABS.register(
            "general", () -> CreativeModeTab.builder().title(Component.translatable(Util.makeTranslationKey("itemGroup", TouhouTinkerModifier.getResource("general"))))
                    .icon(() -> new ItemStack(ItemsRegistry.yinangorb))
                    .displayItems(ItemsRegistry::addCommonTabItems)
                    .build());
    public static final RegistryObject<CreativeModeTab> tabToolParts = CREATIVE_MODE_TABS.register(
            "tool_parts", () -> CreativeModeTab.builder().title(Component.translatable(Util.makeTranslationKey("itemGroup", TouhouTinkerModifier.getResource("tool_parts"))))
                    .icon(() -> {
                        MaterialVariantId material;
                        if (MaterialRegistry.isFullyLoaded()) {
                            material = ToolBuildHandler.RANDOM.getMaterial(GoheiMaterialStats.ID, RandomSource.create());
                        } else {
                            material = ToolBuildHandler.getRenderMaterial(0);
                        }
                        return ItemsRegistry.GoheiCore.get().withMaterialForDisplay(material);
                    })
                    .displayItems(ItemsRegistry::addPartTabItems)
                    .withTabsBefore(tabGeneral.getId())
                    .withSearchBar()
                    .build());
    public static final RegistryObject<CreativeModeTab> tabTools = CREATIVE_MODE_TABS.register(
            "tools", () -> CreativeModeTab.builder().title(Component.translatable(Util.makeTranslationKey("itemGroup", TouhouTinkerModifier.getResource( "tools"))))
                    .icon(() -> ItemsRegistry.Gohei.get().getRenderTool())
                    .displayItems(ItemsRegistry::addToolTabItems)
                    .withTabsBefore(tabToolParts.getId())
                    .withSearchBar()
                    .build());
    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
