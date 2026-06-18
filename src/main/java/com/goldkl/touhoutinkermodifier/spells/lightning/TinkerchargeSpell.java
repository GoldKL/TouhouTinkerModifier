package com.goldkl.touhoutinkermodifier.spells.lightning;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.modifiers.OozoramajutsuModifier;
import com.goldkl.touhoutinkermodifier.modifiers.SecretsealingclubModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Optional;

public class TinkerchargeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "thinkercharge");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setCooldownSeconds(0)
            .setMaxLevel(1)
            .setAllowCrafting(false)
            .setDeprecated(true)
            .build();
    public TinkerchargeSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
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
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.PLAYER_LEVELUP);
    }

    @Override
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        CastResult old = super.canBeCastedBy(spellLevel,castSource,playerMagicData,player);
        if(old.isSuccess()){
            IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(player.getItemBySlot(EquipmentSlot.MAINHAND));
            if(tool == null || tool.getModifier(TTMModifierIds.oozoramajutsu) == ModifierEntry.EMPTY || !(SecretsealingclubModifier.SecretSealingClubcanUse(tool) || tool.hasTag(TagsRegistry.ItemsTag.TECHNOLOGY)))
            {
                return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.touhoutinkermodifier.thinkercharge.cast_error_cannot_thinkercharge").withStyle(ChatFormatting.RED));
            }
            if(ToolEnergyCapability.getEnergy(tool) < 25000)
            {
                return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.touhoutinkermodifier.thinkercharge.cast_error_fe_not_enough").withStyle(ChatFormatting.RED));
            }
        }
        return old;
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(entity.getItemBySlot(EquipmentSlot.MAINHAND));
        if(tool != null && tool.getModifier(TTMModifierIds.oozoramajutsu) != ModifierEntry.EMPTY && (SecretsealingclubModifier.SecretSealingClubcanUse(tool) || tool.hasTag(TagsRegistry.ItemsTag.TECHNOLOGY))){
            boolean flag = tool.getPersistentData().getBoolean(TTMModifierIds.oozoramajutsu);
            OozoramajutsuModifier.updateFlag(tool,!flag);
            ToolEnergyCapability.setEnergy(tool,ToolEnergyCapability.getEnergy(tool) - 25000);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
    @Override
    public boolean allowLooting(){
        return false;
    }
}
