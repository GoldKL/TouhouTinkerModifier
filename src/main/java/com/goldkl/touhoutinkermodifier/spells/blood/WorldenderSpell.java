package com.goldkl.touhoutinkermodifier.spells.blood;

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
import io.redspace.ironsspellbooks.network.spell.ClientboundParticleShockwave;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class WorldenderSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "worldender");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(180)
            .setMaxLevel(3)
            .build();
    public WorldenderSpell() {
        this.baseSpellPower = 200;
        this.spellPowerPerLevel = 60;
        this.castTime = 0;
        this.baseManaCost = 50;
        this.manaCostPerLevel = 30;
    }
    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getSpellPower(spellLevel, caster), 1)));

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
        entity.addEffect(new MobEffectInstance(MobeffectRegistry.WORLDENDER.get(), (int) getSpellPower(spellLevel, entity) , spellLevel - 1, false, false, true));
        if(!world.isClientSide()) {
            MagicManager.spawnParticles(world, new BlastwaveParticleOptions(SchoolRegistry.BLOOD.get().getTargetingColor(), 5), entity.getX(), entity.getY() + .165f, entity.getZ(), 1, 0, 0, 0, 0, true);
            Messages.sendToPlayersTrackingEntity(new ClientboundParticleShockwave(new Vec3(entity.getX(), entity.getY() + .165f, entity.getZ()), 5, ParticleRegistry.BLOOD_GROUND_PARTICLE.get()), entity, true);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}
