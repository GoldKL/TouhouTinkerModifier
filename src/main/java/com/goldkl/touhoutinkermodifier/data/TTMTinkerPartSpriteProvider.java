package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class TTMTinkerPartSpriteProvider extends AbstractPartSpriteProvider {
    public TTMTinkerPartSpriteProvider() {
        super(TouhouTinkerModifier.MODID);
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Parts";
    }

    @Override
    protected void addAllSpites() {
        addPart("gohei_core", GoheiMaterialStats.ID);

        buildTool("spear")
                .withLarge()
                .addBreakableHead("head")
                .addHandle("grip")
                .addHandle("handle")
                .addBinding("accessory");
        buildTool("brass_knuckles")
                .withLarge()
                .addBreakableHead("head1")
                .addBreakableHead("head2")
                .addBreakableHead("head3")
                .addBreakableHead("head4")
                .addPart("protector", PlatingMaterialStats.HELMET.getId());
        buildTool("gohei")
                .addBreakableHead("head")
                .addPart("core", GoheiMaterialStats.ID)
                .addHandle("handle");
    }
}
