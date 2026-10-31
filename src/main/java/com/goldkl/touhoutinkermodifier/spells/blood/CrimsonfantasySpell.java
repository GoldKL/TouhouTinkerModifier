package com.goldkl.touhoutinkermodifier.spells.blood;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CrimsonfantasySpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "crimsonfantasy");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(0)
            .setMaxLevel(0)
            .setAllowCrafting(false)
            .build();
    public CrimsonfantasySpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 10;
        this.castTime = 0;
        this.baseManaCost = 0;
    }
    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {

        double entitySpellPowerModifier = 1;
        double entitySchoolPowerModifier = 1;

        float configPowerModifier = SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.POWER_MULTIPLIER).floatValue();
        if (sourceEntity instanceof LivingEntity livingEntity) {
            entitySpellPowerModifier = (float) livingEntity.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = this.getSchoolType().getPowerFor(livingEntity);
        }

        return (float) (baseSpellPower + spellPowerPerLevel * (spellLevel - 1) + entitySpellPowerModifier * entitySchoolPowerModifier * 50) * configPowerModifier * 0.01f;
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
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.4f);
    }
    @Override
    public boolean allowLooting(){
        return false;
    }
}
