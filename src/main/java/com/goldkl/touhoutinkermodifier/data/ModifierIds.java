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
    private static ModifierId id(String name) {
        return new ModifierId(TouhouTinkerModifier.MODID, name);
    }
}
