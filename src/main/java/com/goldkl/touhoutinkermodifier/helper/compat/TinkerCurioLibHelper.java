package com.goldkl.touhoutinkermodifier.helper.compat;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.module.CurioAttributeModule;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;

public class TinkerCurioLibHelper {
    public static void load(IEventBus modEventBus)
    {
        modEventBus.addListener(TinkerCurioLibHelper::registerSerializers);
    }
    private static void registerSerializers(RegisterEvent event)
    {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(TouhouTinkerModifier.getResource("curio_attribute"), CurioAttributeModule.LOADER);
        }
    }
}
