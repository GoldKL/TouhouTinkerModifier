package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.tools.TinkerToolParts;

public class TTMStationSlotLayoutProvider extends AbstractStationSlotLayoutProvider {
    public TTMStationSlotLayoutProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        Ingredient modifiable = Ingredient.of(TinkerTags.Items.MODIFIABLE);
        defineModifiable(ItemsRegistry.Spear)
                .sortIndex(SORT_WEAPON)
                .addInputItem(TinkerToolParts.smallBlade, 48, 26)
                .addInputItem(TinkerToolParts.toolHandle, 12, 62)
                .addInputItem(TinkerToolParts.toolHandle, 30, 44)
                .addInputItem(TinkerToolParts.toolBinding, 48, 44)
                .build();
        defineModifiable(ItemsRegistry.BrassKnuckles)
                .sortIndex(SORT_WEAPON)
                .addInputItem(TinkerToolParts.smallBlade, 12, 26)
                .addInputItem(TinkerToolParts.smallBlade, 30, 26)
                .addInputItem(TinkerToolParts.smallBlade, 48, 44)
                .addInputItem(TinkerToolParts.smallBlade, 48, 62)
                .addInputItem(TinkerToolParts.plating.get(ArmorItem.Type.HELMET), 30, 44)
                .build();
        defineModifiable(ItemsRegistry.Gohei)
                .sortIndex(SORT_WEAPON)
                .addInputItem(TinkerToolParts.smallBlade, 48, 26)
                .addInputItem(ItemsRegistry.GoheiCore, 30, 44)
                .addInputItem(TinkerToolParts.toughHandle, 12, 62)
                .build();
    }

    @Override
    public String getName() {
        return "Touhou Tinkers Modifier's Tinker Station Slot Layouts";
    }
}
