package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.DamageTypesRegistry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;


public class DamageTypesProvider implements RegistrySetBuilder.RegistryBootstrap<DamageType>{
    public static void register(RegistrySetBuilder builder) {
        builder.add(Registries.DAMAGE_TYPE, new DamageTypesProvider());
    }
    @Override
    public void run(BootstapContext<DamageType> context) {
        context.register(DamageTypesRegistry.radiation_hurt, new DamageType(TouhouTinkerModifier.prefix("radiation_hurt"), DamageScaling.NEVER, 1f, DamageEffects.BURNING));
    }
}
