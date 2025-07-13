package com.goldkl.touhoutinkermodifier.spells.blood;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
@AutoSpellConfig
public class GungnirSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "gungnir");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(0)
            .setMaxLevel(0)
            .setDeprecated(true)
            .build();
    public GungnirSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 0;
    }
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
        return CastType.NONE;
    }
    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.5f);
    }
}
