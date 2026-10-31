package com.goldkl.touhoutinkermodifier.spells.nature;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.util.Log;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InnocentTreasuresSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "innocent_treasures");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setCooldownSeconds(0)
            .setMaxLevel(1)
            .setAllowCrafting(false)
            .build();
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.touhoutinkermodifier.innocent_treasures")
                );
    }
    public InnocentTreasuresSpell() {
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 0;
        this.manaCostPerLevel = 0;
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
    public boolean allowLooting(){
        return false;
    }
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if(entity instanceof ServerPlayer sp){
            if(sp.gameMode.getGameModeForPlayer() == GameType.CREATIVE){
                sp.setGameMode(GameType.SURVIVAL);
            }
            else if(sp.gameMode.getGameModeForPlayer() == GameType.SURVIVAL){
                sp.setGameMode(GameType.CREATIVE);
                sp.getAbilities().flying = true;
                sp.onUpdateAbilities();
            }
        }
        super.onCast(level,spellLevel,entity,castSource,playerMagicData);
    }
}
