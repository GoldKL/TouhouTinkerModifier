package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class ModifierIds {
    public static final ModifierId bibliophilia = id("bibliophilia");
    public static final ModifierId rlyehtext = id("rlyehtext");
    public static final ModifierId holymantle = id("holymantle");
    public static final ModifierId texasnight  = id("texasnight");
    public static final ModifierId buckshotroulette  = id("buckshotroulette");
    public static final ModifierId guzzlord  = id("guzzlord");
    public static final ModifierId twohand  = id("twohand");
    public static final ModifierId ushinokokumairi  = id("ushinokokumairi");
    public static final ModifierId ushinokokumairishichi = id("ushinokokumairishichi");
    public static final ModifierId greeneyes = id("greeneyes");
    public static final ModifierId expelleescanaan = id("expelleescanaan");
    public static final ModifierId tearlaments = id("tearlaments");
    public static final ModifierId scarletdevil = id("scarletdevil");
    public static final ModifierId gungnir = id("gungnir");
    public static final ModifierId laevatein = id("laevatein");

    private static ModifierId id(String name) {
        return new ModifierId(TouhouTinkerModifier.MODID, name);
    }
}
