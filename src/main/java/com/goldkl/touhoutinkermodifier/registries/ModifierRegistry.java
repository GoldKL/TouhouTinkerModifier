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
    //public static final StaticModifier<TwohandModifier> twohand = MODIFIERS.register("twohand", TwohandModifier::new);
    public static final StaticModifier<UshinokokumairiModifier> ushinokokumairi = MODIFIERS.register("ushinokokumairi", UshinokokumairiModifier::new);
    public static final StaticModifier<UshinokokumairishichiModifier> ushinokokumairishichi = MODIFIERS.register("ushinokokumairishichi", UshinokokumairishichiModifier::new);
    public static final StaticModifier<GreeneyesModifier> greeneyes = MODIFIERS.register("greeneyes", GreeneyesModifier::new);
    public static final StaticModifier<ExpelleescanaanModifier> expelleescanaan = MODIFIERS.register("expelleescanaan", ExpelleescanaanModifier::new);
    public static final StaticModifier<TearlamentsModifier> tearlaments = MODIFIERS.register("tearlaments", TearlamentsModifier::new);
    public static final StaticModifier<ScarletdevilModifier> scarletdevil = MODIFIERS.register("scarletdevil", ScarletdevilModifier::new);
    public static final StaticModifier<GungnirModifier>gungnir = MODIFIERS.register("gungnir", GungnirModifier::new);
    public static final StaticModifier<LaevateinModifier>laevatein = MODIFIERS.register("laevatein", LaevateinModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
    }
}
