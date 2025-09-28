package com.goldkl.touhoutinkermodifier.data.material;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class TTMMaterialIds {
    //tier 1
    public static final MaterialId yinangorb = id("yinangorb");
    //tier 2
    public static final MaterialId spellpaper = id("spellpaper");//咒符纸
    public static final MaterialId persuasionneedle = id("persuasionneedle");//封魔针
    public static final MaterialId konpeito = id("konpeito");//金平糖
    public static final MaterialId eighttrigramsfurnace = id("eighttrigramsfurnace");//八卦炉

    private static MaterialId id(String name) {
        return new MaterialId(TouhouTinkerModifier.MODID, name);
    }

}
