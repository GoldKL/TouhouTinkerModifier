package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import net.minecraft.data.PackOutput;
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
        defineModifiable(ItemsRegistry.spear)
                .sortIndex(SORT_WEAPON)
                .addInputItem(TinkerToolParts.smallBlade, 48, 26)
                .addInputItem(TinkerToolParts.toolHandle, 12, 62)
                .addInputItem(TinkerToolParts.toolHandle, 30, 44)
                .addInputItem(TinkerToolParts.toolBinding, 48, 44)
                .build();
    }

    @Override
    public String getName() {
        return "Touhou Tinkers Modifier's Tinker Station Slot Layouts";
    }
}
