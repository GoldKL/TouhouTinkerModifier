package com.goldkl.touhoutinkermodifier.spells.blood;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TTMSoundRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.network.particles.HealParticlesPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TheworldSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "theworld");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(0)
            .setMaxLevel(0)
            .setAllowCrafting(false)
            .setDeprecated(true)
            .build();
    public TheworldSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 34;
        this.baseManaCost = 0;
    }
    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }
    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity){
        return this.getCastTime(spellLevel);
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(TTMSoundRegistry.THE_WORLD_CAST.get());
    }
    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(TTMSoundRegistry.THE_WORLD_FINISH.get());
    }
    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }
    @Override
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        CastResult old = super.canBeCastedBy(spellLevel,castSource,playerMagicData,player);
        if(old.isSuccess() && !player.isCreative() && player.getFoodData().getFoodLevel() == 0){
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.touhoutinkermodifier.cast_error_hungry").withStyle(ChatFormatting.RED));
        }
        return old;
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if(!world.isClientSide)
        {
            if(entity.hasEffect(MobeffectRegistry.TIMESTOP.get()))
            {
                entity.removeEffect(MobeffectRegistry.TIMESTOP.get());
            }
            else
            {
                entity.addEffect(new MobEffectInstance(MobeffectRegistry.TIMESTOP.get(),
                        80 + 20 * spellLevel,
                        spellLevel / 2,
                        false,
                        true));
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
    @Override
    public boolean allowLooting(){
        return false;
    }
}
