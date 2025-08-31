package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.data.AbstractToolItemModelProvider;

import java.io.IOException;

public class TTMToolItemModelProvider extends AbstractToolItemModelProvider {
    public TTMToolItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, existingFileHelper, TouhouTinkerModifier.MODID);
    }
    @Override
    protected void addModels() throws IOException {
        JsonObject toolBlocking = readJson(TouhouTinkerModifier.getResource("base/tool_blocking"));
        JsonObject shieldBlocking = readJson(TouhouTinkerModifier.getResource("base/shield_blocking"));
        tool(ItemsRegistry.spear, toolBlocking, "head");
    }
    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Tool Item Model Provider";
    }
}
