package com.goldkl.touhoutinkermodifier.data.material;

import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class TTMMaterialDataProvider extends AbstractMaterialDataProvider {

    public TTMMaterialDataProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Materials";
    }

    @Override
    protected void addMaterials() {
        addMaterial(TTMMaterialIds.yinangorb,  1, ORDER_WEAPON,  true);
        addMaterial(TTMMaterialIds.spellpaper,  2, ORDER_WEAPON,  true);
        addMaterial(TTMMaterialIds.persuasionneedle,  2, ORDER_WEAPON,  true);
        addMaterial(TTMMaterialIds.konpeito,  2, ORDER_WEAPON,  true);
        addMaterial(TTMMaterialIds.eighttrigramsfurnace,  2, ORDER_WEAPON,  true);
    }
}
