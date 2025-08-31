package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

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
        buildTool("spear")
                .withLarge()
                .addBreakableHead("head")
                .addHandle("grip")
                .addHandle("handle")
                .addBinding("accessory");
    }
}
