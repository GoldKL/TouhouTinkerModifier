package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.modifiers.*;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ModifierRegistry {
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TouhouTinkerModifier.MODID);
    public static final StaticModifier<BibliophiliaModifier> bibliophilia = MODIFIERS.register("bibliophilia", BibliophiliaModifier::new);
    public static final StaticModifier<RlyehtextModifier> rlyehtext = MODIFIERS.register("rlyehtext", RlyehtextModifier::new);
    public static final StaticModifier<HolymantleModifier> holymantle = MODIFIERS.register("holymantle", HolymantleModifier::new);
    public static final StaticModifier<TexasnightModifier> texasnight = MODIFIERS.register("texasnight", TexasnightModifier::new);
    public static final StaticModifier<BuckshotrouletteModifier> buckshotroulette = MODIFIERS.register("buckshotroulette", BuckshotrouletteModifier::new);
    public static final StaticModifier<GuzzlordModifier> guzzlord = MODIFIERS.register("guzzlord", GuzzlordModifier::new);
    public static final StaticModifier<TwohandModifier> twohand = MODIFIERS.register("twohand", TwohandModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
    }
}
