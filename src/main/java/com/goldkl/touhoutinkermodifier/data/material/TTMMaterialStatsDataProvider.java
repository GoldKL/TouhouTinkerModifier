package com.goldkl.touhoutinkermodifier.data.material;

import com.goldkl.touhoutinkermodifier.registries.TTMBulletTypeRegistry;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;

public class TTMMaterialStatsDataProvider extends AbstractMaterialStatsDataProvider {
    public TTMMaterialStatsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialStats() {
        //tier 1
        addMaterialStats(TTMMaterialIds.yinangorb,
                new GoheiMaterialStats(200, TTMBulletTypeRegistry.DefaultBullet));
        //tier 2
        addMaterialStats(TTMMaterialIds.spellpaper,
                new GoheiMaterialStats(400, TTMBulletTypeRegistry.ReimuA.get()));
        addMaterialStats(TTMMaterialIds.persuasionneedle,
                new GoheiMaterialStats(400, TTMBulletTypeRegistry.ReimuB.get()));
        addMaterialStats(TTMMaterialIds.konpeito,
                new GoheiMaterialStats(400, TTMBulletTypeRegistry.MarisaA.get()));
        addMaterialStats(TTMMaterialIds.eighttrigramsfurnace,
                new GoheiMaterialStats(400, TTMBulletTypeRegistry.MarisaB.get()));
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Material Stats";
    }
}
