package com.goldkl.touhoutinkermodifier.data.material;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class TTMMaterialRenderInfoProvider  extends AbstractMaterialRenderInfoProvider {
    public TTMMaterialRenderInfoProvider(PackOutput packOutput, AbstractMaterialSpriteProvider spriteProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, spriteProvider, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        //tier 1
        buildRenderInfo(TTMMaterialIds.yinangorb)
                .color(0xff0000)
                .fallbacks("crystal", "rock");
        //tier 2
        buildRenderInfo(TTMMaterialIds.spellpaper)
                .color(0xf85a47);
        buildRenderInfo(TTMMaterialIds.persuasionneedle)
                .color(0xc82a37)
                .fallbacks("metal");
        buildRenderInfo(TTMMaterialIds.konpeito)
                .color(0xffd800)
                .fallbacks("crystal");
        buildRenderInfo(TTMMaterialIds.eighttrigramsfurnace)
                .color(0x212121)
                .fallbacks("metal");
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Material Render Info Provider";
    }
}
