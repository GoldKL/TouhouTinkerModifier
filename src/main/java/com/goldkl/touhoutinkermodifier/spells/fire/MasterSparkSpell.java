package com.goldkl.touhoutinkermodifier.spells.fire;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.entity.spell.fire.masterspark.MasterSparkVisualEntity;
import com.goldkl.touhoutinkermodifier.helper.SpellDamageSourceInterface;
import com.goldkl.touhoutinkermodifier.registries.TTMSoundRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.network.particles.ShockwaveParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MasterSparkSpell extends AbstractSpell {
    //等级大于3：不需咏唱
    //等级大于6：法穿100%（仅限法抗大于0的部分）
    //等级大于9：镜头可转
    private final ResourceLocation spellId = TouhouTinkerModifier.getResource("master_spark");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setCooldownSeconds(300)
            .setMaxLevel(9)
            .setAllowCrafting(false)
            .build();
    public MasterSparkSpell() {
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 6;
        this.castTime = 200;
        this.baseManaCost = 50;
        this.manaCostPerLevel = 50;
    }
    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRange(spellLevel, caster), 1)));
        list.add(Component.translatable("ui.touhoutinkermodifier.quarter", Utils.stringTruncation(getDamage(spellLevel, caster), 1)));
        if(spellLevel >= 3)list.add(Component.translatable("ui.touhoutinkermodifier.no_casting_time"));
        if(spellLevel >= 6)list.add(Component.translatable("ui.touhoutinkermodifier.passby_resist"));
        if(spellLevel >= 9)list.add(Component.translatable("ui.touhoutinkermodifier.better_camera"));
        return list.stream().toList();
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
        return CastType.CONTINUOUS;
    }
    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }
    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        float entityCastTimeModifier = spellLevel >= 3 ? 0.5f : 1;
        return Math.round(this.getCastTime(spellLevel) * entityCastTimeModifier);
    }
    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData!=null && playerMagicData.getCastDurationRemaining() <= 100) {
            if (playerMagicData.getCastDurationRemaining() == 99){
                level.addFreshEntity(new MasterSparkVisualEntity(level, getRange(spellLevel, entity), entity));
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), TTMSoundRegistry.MASTER_SPARK_TRUE_CAST.get(), SoundSource.PLAYERS, 0.5F, 1F);
            }
            if (playerMagicData.getCastDurationRemaining() % 5 == 0 ) {
                Vec3 start = entity.getEyePosition();
                Vec3 end = entity.getLookAngle().normalize().scale(getRange(spellLevel, entity)).add(start);
                Vec3 direction = end.subtract(start).normalize().scale(2);
                start = start.subtract(direction);
                AABB range = entity.getBoundingBox().expandTowards(end.subtract(start));
                List<HitResult> hits = new ArrayList<>();
                List<? extends Entity> entities = level.getEntities(entity, range, Utils::canHitWithRaycast);
                for (Entity target : entities) {
                    HitResult hit = Utils.checkEntityIntersecting(target, start, end, 2f);
                    if (hit.getType() != HitResult.Type.MISS) {
                        hits.add(hit);
                    }
                }
                for(HitResult hit : hits){
                    if(hit.getType() == HitResult.Type.ENTITY){
                        Entity target = ((EntityHitResult) hit).getEntity();
                        DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(spellLevel,entity));
                        MagicManager.spawnParticles(level, ParticleHelper.ELECTRIC_SPARKS, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 50, 0, 0, 0, .3, false);
                    }
                }
            }
        }
    }
    public static float getRange(int level, LivingEntity caster) {
        return 15 + 5 * level;
    }
    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }
    private SpellDamageSource getDamageSource(int spellLevel, LivingEntity caster) {
        if(spellLevel >= 6) return ((SpellDamageSourceInterface)getDamageSource(caster)).touhouTinkerModifier$setPassbyResist(1f).setIFrames(0);
        return getDamageSource(caster).setIFrames(0);
    }
    @Override
    public boolean allowLooting(){
        return false;
    }
}
