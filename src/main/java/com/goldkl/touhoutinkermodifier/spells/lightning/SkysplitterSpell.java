package com.goldkl.touhoutinkermodifier.spells.lightning;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.entity.spell.lightning.SkysplitterEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class SkysplitterSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "skysplitter");
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRadius(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.aoe_damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)).append(" + ").append(Component.translatable("ui.touhoutinkermodifier.maxhealth_percent", Utils.stringTruncation(getPercenthealthdamagetoInfo(spellLevel, caster), 1))),
                Component.translatable("ui.irons_spellbooks.absorption", Utils.stringTruncation(getabsorb(spellLevel, caster), 1)).append(" + ").append(Component.translatable("ui.touhoutinkermodifier.maxhealth_percent", Utils.stringTruncation(getPercenthealthabsorbtoInfo(spellLevel, caster), 1))));
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setCooldownSeconds(15)
            .setMaxLevel(6)
            .build();
    private final float percentage;
    private final float percenthealth;
    private final float percenthealthperlevel;
    private final float percenthealthdamage;
    private final float percenthealthdamageperlevel;
    public SkysplitterSpell() {
        this.baseSpellPower = 15;
        this.spellPowerPerLevel = 5;
        this.castTime = 0;
        this.baseManaCost = 30;
        this.manaCostPerLevel = 5;
        this.percentage = 0.8f;
        this.percenthealth = 0.1f;
        this.percenthealthperlevel = 0.01f;
        this.percenthealthdamage = 0.05f;
        this.percenthealthdamageperlevel = 0.005f;
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
        return CastType.INSTANT;
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            var target = castTargetingData.getTarget((ServerLevel) world);
            if (target != null)
                spawn = target.position();
        }
        if (spawn == null) {
            spawn = Utils.raycastForEntity(world, entity, 32, true, .15f).getLocation();
            spawn = Utils.moveToRelativeGroundLevel(world, spawn, 6);
        }


        float damage = this.getDamage(spellLevel,entity);
        float radius = this.getRadius(spellLevel,entity);
        float absorb = this.getabsorb(spellLevel,entity);
        SkysplitterEntity aoeEntity = new SkysplitterEntity(world,entity,damage,radius,absorb,spellLevel);
        aoeEntity.setDuration(40);
        aoeEntity.setPos(spawn);
        world.addFreshEntity(aoeEntity);

        TargetedAreaEntity visualEntity = TargetedAreaEntity.createTargetAreaEntity(world, spawn, radius, 0x2bcaf6);
        visualEntity.setDuration(40);
        visualEntity.setOwner(aoeEntity);

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker);
    }
    public float getDamage(int spellLevel, LivingEntity caster) {
        return (1.0f - percentage) * getSpellPower(spellLevel, null) + percentage * getSpellPower(spellLevel, caster);
    }
    public float getPercenthealthdamage(int spellLevel, LivingEntity target) {
        return target.getMaxHealth() * (this.percenthealthdamage + this.percenthealthdamageperlevel * spellLevel);
    }
    public float getRadius(int spellLevel, LivingEntity caster) {
        return 4 + spellLevel * 0.25f;
    }
    public float getabsorb(int spellLevel, LivingEntity caster) {
        return percentage * getSpellPower(spellLevel, caster) * 0.5f;
    }
    public float getPercenthealthabsorb(int spellLevel, LivingEntity target){
        return target.getMaxHealth() * (this.percenthealth + this.percenthealthperlevel * spellLevel);
    }
    public float getPercenthealthdamagetoInfo(int spellLevel, LivingEntity target){
        return 100 * (this.percenthealthdamage + this.percenthealthdamageperlevel * spellLevel);
    }
    public float getPercenthealthabsorbtoInfo(int spellLevel, LivingEntity target){
        return 100 * (this.percenthealth + this.percenthealthperlevel * spellLevel);
    }
}
