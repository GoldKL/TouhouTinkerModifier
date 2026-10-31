package com.goldkl.touhoutinkermodifier.spells.ender;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.modifiers.MidnightbirdModifier;
import com.goldkl.touhoutinkermodifier.modifiers.OozoramajutsuModifier;
import com.goldkl.touhoutinkermodifier.modifiers.SecretsealingclubModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class MidNightBirdSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "mid_night_bird");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setCooldownSeconds(10)
            .setMaxLevel(8)
            .setAllowCrafting(false)
            .build();
    public MidNightBirdSpell() {
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 50;
        this.manaCostPerLevel = 10;
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
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        CastResult old = super.canBeCastedBy(spellLevel,castSource,playerMagicData,player);
        if(old.isSuccess()){
            for(EquipmentSlot slot: EquipmentSlot.values()){
                IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(player.getItemBySlot(slot));
                if(tool != null && tool.getModifier(TTMModifierIds.midnightbird) != ModifierEntry.EMPTY){
                    return old;
                }
            }
        }
        return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.touhoutinkermodifier.thinkercharge.cast_error_fe_not_enough").withStyle(ChatFormatting.RED));
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        boolean flag = false;
        for(EquipmentSlot slot: EquipmentSlot.values()){
            IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(entity.getItemBySlot(slot));
            if(tool != null && tool.getModifier(TTMModifierIds.midnightbird) != ModifierEntry.EMPTY){
                flag |= tool.getPersistentData().getBoolean(TTMModifierIds.midnightbird);
            }
        }
        for(EquipmentSlot slot: EquipmentSlot.values()){
            IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(entity.getItemBySlot(slot));
            if(tool != null && tool.getModifier(TTMModifierIds.midnightbird) != ModifierEntry.EMPTY){
                MidnightbirdModifier.updateFlag(tool,!flag);
            }
        }
        if(!world.isClientSide()) {
            MagicManager.spawnParticles(world, new BlastwaveParticleOptions(SchoolRegistry.ENDER.get().getTargetingColor(), 5), entity.getX(), entity.getY() + .165f, entity.getZ(), 1, 0, 0, 0, 0, true);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
    @Override
    public boolean allowLooting(){
        return false;
    }
}