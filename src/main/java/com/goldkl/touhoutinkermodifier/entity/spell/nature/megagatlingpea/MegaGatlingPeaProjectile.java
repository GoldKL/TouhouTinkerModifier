package com.goldkl.touhoutinkermodifier.entity.spell.nature.megagatlingpea;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Supplier;

public class MegaGatlingPeaProjectile extends AbstractMagicProjectile {
    public static enum PeaKind{
        NORMAL,
        FIRE,
        SNOW,
        THREE,
        ELECTRIC,
        GOO
    }
    private float damage;
    private PeaKind kind;
    protected MegaGatlingPeaProjectile(EntityType<? extends MegaGatlingPeaProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
    public void setKind(PeaKind kind) {
        this.kind = kind;
    }

    @Override
    public void trailParticles() {

    }

    @Override
    public void impactParticles(double x, double y, double z) {

    }

    @Override
    public float getSpeed() {
        return 1f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void defineSynchedData() {

    }
}
