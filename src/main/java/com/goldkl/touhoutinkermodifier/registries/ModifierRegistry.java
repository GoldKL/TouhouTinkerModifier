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
    public static final StaticModifier<YoungscarletmoonModifier>youngscarletmoon = MODIFIERS.register("youngscarletmoon", YoungscarletmoonModifier::new);
    public static final StaticModifier<UnderredmoonModifier>underredmoon = MODIFIERS.register("underredmoon", UnderredmoonModifier::new);
    public static final StaticModifier<AndthentherewerenoneModifier>andthentherewerenone = MODIFIERS.register("andthentherewerenone", AndthentherewerenoneModifier::new);
    public static final StaticModifier<CrimsonfantasyModifier>crimsonfantasy = MODIFIERS.register("crimsonfantasy", CrimsonfantasyModifier::new);
    public static final StaticModifier<EverbrightscarlettowerModifier>everbrightscarlettower = MODIFIERS.register("everbrightscarlettower", EverbrightscarlettowerModifier::new);
    public static final StaticModifier<DevillibrarianModifier>devillibrarian = MODIFIERS.register("devillibrarian", DevillibrarianModifier::new);
    public static final StaticModifier<OverpowermqModifier>overpowermq = MODIFIERS.register("overpowermq", OverpowermqModifier::new);
    public static final StaticModifier<SevenluminarieswizardModifier>sevenluminarieswizard = MODIFIERS.register("sevenluminarieswizard", SevenluminarieswizardModifier::new);
    public static final StaticModifier<RedblacktreeModifier>redblacktree = MODIFIERS.register("redblacktree", RedblacktreeModifier::new);
    public static final StaticModifier<KillerdollModifier>killerdoll = MODIFIERS.register("killerdoll", KillerdollModifier::new);
    public static final StaticModifier<VanhelsingprogenyModifier>vanhelsingprogeny = MODIFIERS.register("vanhelsingprogeny", VanhelsingprogenyModifier::new);
    public static final StaticModifier<JacktheripperModifier>jacktheripper = MODIFIERS.register("jacktheripper",JacktheripperModifier::new);
    public static final StaticModifier<PerfectandelegantModifier>perfectandelegant = MODIFIERS.register("perfectandelegant",PerfectandelegantModifier::new);
    public static final StaticModifier<PocketwatchofbloodModifier>pocketwatchofblood = MODIFIERS.register("pocketwatchofblood",PocketwatchofbloodModifier::new);
    public static final StaticModifier<TheworldModifier>theworld = MODIFIERS.register("theworld",TheworldModifier::new);
    public static final StaticModifier<RainbowtaichiModifier>rainbowtaichi = MODIFIERS.register("rainbowtaichi",RainbowtaichiModifier::new);
    public static final StaticModifier<FanghuaxuanlanModifier>fanghuaxuanlan = MODIFIERS.register("fanghuaxuanlan",FanghuaxuanlanModifier::new);
    public static final StaticModifier<BengshancaijiModifier>bengshancaiji = MODIFIERS.register("bengshancaiji",BengshancaijiModifier::new);
    public static final StaticModifier<MidnightbirdModifier>midnightbird = MODIFIERS.register("midnightbird",MidnightbirdModifier::new);
    public static final StaticModifier<SonanokaModifier>sonanoka = MODIFIERS.register("sonanoka",SonanokaModifier::new);
    public static final StaticModifier<ApparitionsstalkthenightModifier>apparitionsstalkthenight = MODIFIERS.register("apparitionsstalkthenight",ApparitionsstalkthenightModifier::new);
    public static final StaticModifier<DevourdarknessModifier>devourdarkness = MODIFIERS.register("devourdarkness",DevourdarknessModifier::new);
    public static final StaticModifier<KojisousamadesitaModifier>kojisousamadesita = MODIFIERS.register("kojisousamadesita",KojisousamadesitaModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
    }
}
