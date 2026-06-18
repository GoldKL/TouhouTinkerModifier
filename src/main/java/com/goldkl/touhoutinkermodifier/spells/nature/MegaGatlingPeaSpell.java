package com.goldkl.touhoutinkermodifier.spells.nature;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.resources.ResourceLocation;

public class MegaGatlingPeaSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "megagatlingpea");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setCooldownSeconds(90)
            .setMaxLevel(5)
            .build();
    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }
    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }
}
