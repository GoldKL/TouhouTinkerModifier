package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.modifiers.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ModifierRegistry {
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TouhouTinkerModifier.MODID);
    //通用
    public static final StaticModifier<LongspearModifier> longspear = MODIFIERS.register("longspear", LongspearModifier::new);
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
    //public static final StaticModifier<YoungscarletmoonModifier>youngscarletmoon = MODIFIERS.register("youngscarletmoon", YoungscarletmoonModifier::new);
    public static final StaticModifier<AndthentherewerenoneModifier>andthentherewerenone = MODIFIERS.register("andthentherewerenone", AndthentherewerenoneModifier::new);
    public static final StaticModifier<CrimsonfantasyModifier>crimsonfantasy = MODIFIERS.register("crimsonfantasy", CrimsonfantasyModifier::new);
    public static final StaticModifier<EverbrightscarlettowerModifier>everbrightscarlettower = MODIFIERS.register("everbrightscarlettower", EverbrightscarlettowerModifier::new);
    public static final StaticModifier<DevillibrarianModifier>devillibrarian = MODIFIERS.register("devillibrarian", DevillibrarianModifier::new);
    public static final StaticModifier<OverpowermqModifier>overpowermq = MODIFIERS.register("overpowermq", OverpowermqModifier::new);
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
    public static final StaticModifier<AvatarofdarknessModifier>avatarofdarkness = MODIFIERS.register("avatarofdarkness",AvatarofdarknessModifier::new);
    public static final StaticModifier<JikicoercionModifier>jikicoercion = MODIFIERS.register("jikicoercion", JikicoercionModifier::new);
    public static final StaticModifier<FullfirepowerModifier>fullfirepower = MODIFIERS.register("fullfirepower",FullfirepowerModifier::new);
    public static final StaticModifier<ArcticstormModifier>arcticstorm = MODIFIERS.register("arcticstorm",ArcticstormModifier::new);
    public static final StaticModifier<RiverofdeathModifier>riverofdeath = MODIFIERS.register("riverofdeath",RiverofdeathModifier::new);
    //地灵殿
    public static final StaticModifier<OverroseModifier>overrose = MODIFIERS.register("overrose",OverroseModifier::new);
    public static final StaticModifier<MissmaryModifier>missmary = MODIFIERS.register("missmary",MissmaryModifier::new);
    public static final StaticModifier<KoishiseyeModifier>koishiseye =  MODIFIERS.register("koishiseye",KoishiseyeModifier::new);
    public static final StaticModifier<LoveburiedembersModifier>loveburiedembers = MODIFIERS.register("loveburiedembers",LoveburiedembersModifier::new);
    public static final StaticModifier<WelldestructorModifier>welldestructor = MODIFIERS.register("welldestructor",WelldestructorModifier::new);
    public static final StaticModifier<SuperegoinhibitionModifier>superegoinhibition = MODIFIERS.register("superegoinhibition",SuperegoinhibitionModifier::new);
    public static final StaticModifier<IdliberationModifier>idliberation = MODIFIERS.register("idliberation",IdliberationModifier::new);
    public static final StaticModifier<TriplefatalityModifier>triplefatality = MODIFIERS.register("triplefatality",TriplefatalityModifier::new);
    public static final StaticModifier<ThepowerofmountainModifier> thepowerofmountain = MODIFIERS.register("thepowerofmountain",ThepowerofmountainModifier::new);
    public static final StaticModifier<TokamakModifier> tokamak = MODIFIERS.register("tokamak",TokamakModifier::new);
    public static final StaticModifier<UndergroundsunModifier> undergroundsun = MODIFIERS.register("undergroundsun",UndergroundsunModifier::new);
    public static final StaticModifier<ThousandtrillionflareModifier>thousandtrillionflare = MODIFIERS.register("thousandtrillionflare",ThousandtrillionflareModifier::new);
    public static final StaticModifier<HellcrowModifier>hellcrow = MODIFIERS.register("hellcrow",HellcrowModifier::new);
    public static final StaticModifier<TerriblesouvenirModifier>terriblesouvenir = MODIFIERS.register("terriblesouvenir",TerriblesouvenirModifier::new);
    public static final StaticModifier<OverthinkingModifier>overthinking = MODIFIERS.register("overthinking",OverthinkingModifier::new);
    public static final StaticModifier<KomeijisistersModifier>komeijisisters = MODIFIERS.register("komeijisisters",KomeijisistersModifier::new);
    public static final StaticModifier<RekindlingofdeadashesModifier>rekindlingofdeadashes = MODIFIERS.register("rekindlingofdeadashes",RekindlingofdeadashesModifier::new);
    public static final StaticModifier<HellstrafficaccidentModifier>hellstrafficaccident = MODIFIERS.register("hellstrafficaccident",HellstrafficaccidentModifier::new);
    //锦上京
    public static final StaticModifier<DeepbaloneyModifier>deepbaloney = MODIFIERS.register("deepbaloney",DeepbaloneyModifier::new);
    //风神录
    public static final StaticModifier<HarvestjoyModifier>harvestjoy = MODIFIERS.register("harvestjoy",HarvestjoyModifier::new);
    public static final StaticModifier<ThefadingautumnModifier>thefadingautumn = MODIFIERS.register("thefadingautumn",ThefadingautumnModifier::new);
    //花映冢
    public static final StaticModifier<RequiemofsoulsModifier>requiemofsouls = MODIFIERS.register("requiemofsouls",RequiemofsoulsModifier::new);
    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
    }
}
