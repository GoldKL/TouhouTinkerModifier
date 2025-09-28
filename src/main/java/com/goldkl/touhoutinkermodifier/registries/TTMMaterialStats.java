package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;

public class TTMMaterialStats {
    public static void init() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        registry.registerStatType(GoheiMaterialStats.TYPE, MaterialRegistry.MELEE_HARVEST);
    }
}
