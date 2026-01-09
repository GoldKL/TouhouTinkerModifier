package com.goldkl.touhoutinkermodifier.spells.ice;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.communication.spells.ice.ClientArcticstormParticles;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.tracking.ChannelEventTracker;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArcticstormSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "arcticstorm");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
            .setCooldownSeconds(10)
            .setMaxLevel(6)
            .build();
    public ArcticstormSpell() {
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 4;
        this.castTime = 0;
        this.baseManaCost = 30;
        this.manaCostPerLevel = 5;
    }
    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRadius(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.aoe_damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.cast_continuous", Utils.timeFromTicks(getSpelltick(spellLevel, caster), 1)));
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
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker);
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobeffectRegistry.ARCTICSTROM.get(), getSpelltick(spellLevel, entity) , spellLevel - 1, false, false, true));
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
    public void applyDamage(int spellLevel, LivingEntity caster)
    {
        float radius = getRadius(spellLevel, caster);
        float damage = getDamage(spellLevel, caster);
        if(caster.tickCount % 5 == 0)
        {
            ChannelEventTracker.sendToPlayersTrackingEntity(new ClientArcticstormParticles(caster.position(),radius),caster,true);
        }
        Level level = caster.level();
        level.getEntities(caster, caster.getBoundingBox().inflate(radius, 2, radius), (target) -> !DamageSources.isFriendlyFireBetween(target, caster) && Utils.hasLineOfSight(level, caster, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(caster) < radius * radius) {
                if(caster.tickCount % 10 == 0){
                    MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * .5f, livingEntity.getZ(), 50, livingEntity.getBbWidth() * .5f, livingEntity.getBbHeight() * .5f, livingEntity.getBbWidth() * .5f, .03, false);
                    TTMEntityUtils.clearLivingEntityInvulnerableTime(livingEntity);
                    DamageSources.applyDamage(livingEntity, damage, this.getDamageSource(caster));
                }
                livingEntity.setTicksFrozen(Math.min(livingEntity.getTicksRequiredToFreeze(), livingEntity.getTicksFrozen() + 4));
            }
        });
    }
    public float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * 0.375f;
    }
    public float getRadius(int spellLevel, LivingEntity caster) {
        return 3 + (float) spellLevel / 2;
    }
    public int getSpelltick(int spellLevel, LivingEntity caster)
    {
        double entitySpellPowerModifier = 1.0F;
        double entitySchoolPowerModifier = 1.0F;
        if (caster !=null) {
            entitySpellPowerModifier = caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = this.getSchoolType().getPowerFor(caster);
        }
        return (int) (80 * entitySpellPowerModifier * entitySchoolPowerModifier);
    }
}
