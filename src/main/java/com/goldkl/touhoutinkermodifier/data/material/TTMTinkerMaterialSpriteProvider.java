package com.goldkl.touhoutinkermodifier.data.material;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToSpriteTransformer;


public class TTMTinkerMaterialSpriteProvider extends AbstractMaterialSpriteProvider {

    @Override
    public String getName() {
        return "Touhou Tinker Modifier' Materials";
    }

    @Override
    protected void addAllMaterials() {
        //tier 1
        ResourceLocation yinanggorbbaseTexture = TouhouTinkerModifier.getResource("generator/yinangorb");
        ResourceLocation yinanggorbhighlightTexture = TouhouTinkerModifier.getResource("generator/yinangorb_highlight");
        ResourceLocation yinanggorbborderTexture = TouhouTinkerModifier.getResource("generator/yinangorb_border");
        buildMaterial(TTMMaterialIds.yinangorb)
                .statType(GoheiMaterialStats.ID)
                .repairKit()
                .fallbacks("crystal", "rock")
                .transformer(GreyToSpriteTransformer.builderFromBlack()
                        .addTexture( 63, yinanggorbborderTexture, 0xffc8c8c8).addTexture(102, yinanggorbborderTexture)
                        .addTexture(140, yinanggorbbaseTexture, 0xffe1e1e1).addTexture(178, yinanggorbbaseTexture)
                        .addTexture(216, yinanggorbhighlightTexture, 0xffe1e1e1).addTexture(255, yinanggorbhighlightTexture)
                        .build());
        //tier 2
        buildMaterial(TTMMaterialIds.spellpaper)
                .statType(GoheiMaterialStats.ID)
                .repairKit()
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFFd32929)
                        .addARGB(102, 0xFFff3131)
                        .addARGB(140, 0xFFffbbbb)
                        .addARGB(178, 0xFFc8c8c8)
                        .addARGB(216, 0xFFe5e5e5)
                        .addARGB(255, 0xFFffffff)
                        .build());
        buildMaterial(TTMMaterialIds.persuasionneedle)
                .statType(GoheiMaterialStats.ID)
                .repairKit()
                .fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFFa31414)
                        .addARGB(102, 0xFFb92c2c)
                        .addARGB(140, 0xFFd34e4e)
                        .addARGB(178, 0xFFeb7676)
                        .addARGB(216, 0xFFffa8a8)
                        .addARGB(255, 0xFFffffff)
                        .build());
        buildMaterial(TTMMaterialIds.konpeito)
                .statType(GoheiMaterialStats.ID)
                .repairKit()
                .fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF4f4301)
                        .addARGB(102, 0xFF806c01)
                        .addARGB(140, 0xFFaf9401)
                        .addARGB(178, 0xFFdfbc01)
                        .addARGB(216, 0xFFffe13d)
                        .addARGB(255, 0xFFffffff)
                        .build());
        ResourceLocation eighttrigramsfurnacebaseTexture = TouhouTinkerModifier.getResource("generator/eighttrigramsfurnace");
        buildMaterial(TTMMaterialIds.eighttrigramsfurnace)
                .statType(GoheiMaterialStats.ID)
                .repairKit()
                .fallbacks("crystal", "rock")
                .transformer(GreyToSpriteTransformer.builderFromBlack()
                        .addTexture( 63, eighttrigramsfurnacebaseTexture,    0xFF2b2b2b).addTexture(102, eighttrigramsfurnacebaseTexture,0xff575757)
                        .addTexture(140, eighttrigramsfurnacebaseTexture,      0xFF828282).addTexture(178, eighttrigramsfurnacebaseTexture, 0xffadadad)
                        .addTexture(216, eighttrigramsfurnacebaseTexture, 0xFFd9d9d9).addTexture(255, eighttrigramsfurnacebaseTexture)
                        .build());
    }
}
