package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.mobeffect.HolymantleEffect;
import com.goldkl.touhoutinkermodifier.mobeffect.FragileEffect;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobeffectRegistry {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TouhouTinkerModifier.MODID);
    public static final RegistryObject<MobEffect> HOLYMANTLE = REGISTRY.register("holymantle", HolymantleEffect::new);
    public static final RegistryObject<MobEffect> FRAGILE = REGISTRY.register("fragile", FragileEffect::new);
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
