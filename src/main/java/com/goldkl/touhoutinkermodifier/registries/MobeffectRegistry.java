package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.mobeffect.*;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobeffectRegistry {
    private static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TouhouTinkerModifier.MODID);
    public static final RegistryObject<MobEffect> HOLYMANTLE = REGISTRY.register("holymantle", HolymantleEffect::new);
    public static final RegistryObject<MobEffect> FRAGILE = REGISTRY.register("fragile", FragileEffect::new);
    public static final RegistryObject<MobEffect> IMPRISON = REGISTRY.register("imprison",ImprisonEffect::new);
    public static final RegistryObject<MobEffect> TIMESTOP = REGISTRY.register("timestop", TimestopEffect::new);
    public static final RegistryObject<MobEffect> INTERNALINJURY = REGISTRY.register("internal_injury",InternalinjuryEffect::new);
    public static final RegistryObject<MobEffect> SONANOKA = REGISTRY.register("sonanoka", SonanokaEffect::new);
    public static final RegistryObject<MobEffect> BREAKDARKNESS = REGISTRY.register("breakdarkness", BreakdarknessEffect::new);
    public static final RegistryObject<MobEffect> MELT = REGISTRY.register("melt", MeltEffect::new);
    public static final RegistryObject<MobEffect> ARCTICSTROM = REGISTRY.register("arcticstorm", ArcticstormEffect::new);
    public static final RegistryObject<MobEffect> BROKENPOCKETWATCH = REGISTRY.register("brokenpocketwatch", BrokenpocketwatchEffect::new);
    public static final RegistryObject<MobEffect> WORLDENDER = REGISTRY.register("worldender", WorldenderEffect::new);
    public static final RegistryObject<MobEffect> EXHAUSTED = REGISTRY.register("exhausted", ExhaustedEffect::new);
    public static final RegistryObject<MobEffect> TRIPLEFATALITY = REGISTRY.register("triplefatality", TriplefatalityEffect::new);
    public static final RegistryObject<MobEffect> FINEDRUNK = REGISTRY.register("finedrunk", FinedrunkEffect::new);
    public static final RegistryObject<MobEffect> DEATHFIRE = REGISTRY.register("deathfire", DeathfireEffect::new);
    public static final RegistryObject<MobEffect> RADIATION = REGISTRY.register("radiation", RadiationEffect::new);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
