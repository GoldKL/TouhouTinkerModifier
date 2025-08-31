package com.goldkl.touhoutinkermodifier.entity.spell.lightning;

import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.spells.lightning.SkysplitterSpell;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

public class SkysplitterEntity extends AoeEntity implements AntiMagicSusceptible {
    public SkysplitterEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
    }

    public SkysplitterEntity(Level level, LivingEntity owner, float damage, float radius,float absorb,int spelllevel) {
        this(EntitiesRegistry.SkySplitter.get(), level);
        this.setOwner(owner);
        this.setRadius(radius);
        this.setDamage(damage);
        this.absorb = absorb;
        this.spelllevel = spelllevel;
    }
    private float absorb;
    private int spelllevel;
    @Override
    public void applyEffect(LivingEntity livingEntity) {
    }

    public final int waitTime = 40;

    @Override
    public void tick() {
        Level level = this.level();
        if (tickCount == waitTime) {
            this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 10000f, 0.8F + this.random.nextFloat() * 0.2F);
            this.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);

            if (!level.isClientSide) {
                var center = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, center.x, center.y, center.z, 40, 0, 0, 0, 0.5, false);
                MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SpellsRegistry.skysplitter.get().getSchoolType().getTargetingColor(), this.getRadius()), center.x, center.y, center.z, 1, 0, 0, 0, 0, true);
                float explosionRadius = this.getRadius();
                var explosionRadiusSqr = explosionRadius * explosionRadius;
                var entities = level.getEntities(this, this.getBoundingBox().inflate(explosionRadius));
                for (Entity entity : entities) {
                    double distanceSqr = entity.distanceToSqr(center);
                    if (distanceSqr < explosionRadiusSqr && canHitEntity(entity) && (this.getOwner() == null || !DamageSources.isFriendlyFireBetween(entity, this.getOwner()))) {
                        float damage = this.damage;
                        if(entity instanceof LivingEntity livingEntity) {
                            damage += ((SkysplitterSpell)SpellsRegistry.skysplitter.get()).getPercenthealthdamage(this.spelllevel,livingEntity);
                        }
                        DamageSources.applyDamage(entity, damage, SpellsRegistry.skysplitter.get().getDamageSource(this, getOwner()));
                    }
                    if(distanceSqr < explosionRadiusSqr && this.getOwner() != null && entity == this.getOwner())
                    {
                        if(entity instanceof LivingEntity livingEntity) {
                            float absorb = this.absorb + ((SkysplitterSpell)SpellsRegistry.skysplitter.get()).getPercenthealthabsorb(this.spelllevel,livingEntity);
                            if(absorb > 0.0f)
                                TTMEntityUtils.addLivingEntityAbsorptionAmountByMax(livingEntity, absorb);
                        }
                    }
                }
            }
        } else if (tickCount > waitTime) {
            discard();
        }
        else
        {
            if (level.isClientSide) {
                this.ambientParticles();
            }
        }
    }
    @Override
    public float getParticleCount() {
        return (float) tickCount / waitTime;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleHelper.ELECTRICITY);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Spelllevel", this.spelllevel);
        pCompound.putFloat("Absorb", this.absorb);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.spelllevel = pCompound.getInt("Spelllevel");
        this.absorb = pCompound.getFloat("Absorb");
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.discard();
    }
}
