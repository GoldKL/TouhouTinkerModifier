package com.goldkl.touhoutinkermodifier.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuCommander;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverInfo;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@SerialClass
public class TrackDanmakuEntity extends ModifiableDamakuEntity {
    @Nullable
    @SerialClass.SerialField
    private UUID target;
    private static final EntityDataAccessor<OptionalInt> DATA_TARGET_ID = SynchedEntityData.defineId(TrackDanmakuEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    public TrackDanmakuEntity(EntityType<? extends TrackDanmakuEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public TrackDanmakuEntity(EntityType<? extends TrackDanmakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public TrackDanmakuEntity(EntityType<? extends TrackDanmakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET_ID, OptionalInt.empty());
    }

    public Optional<Entity> getTrackTarget() {
        return this.entityData.get(DATA_TARGET_ID).stream().mapToObj(this.level()::getEntity).filter(Objects::nonNull).findFirst();
    }

    public void setTrackTarget(Entity target) {
        this.entityData.set(DATA_TARGET_ID, OptionalInt.of(target.getId()));
    }

    public void eraseTrackTarget() {
        this.entityData.set(DATA_TARGET_ID, OptionalInt.empty());
    }
    @Override
    public void tick()
    {
        super.tick();
        if(target != null)
        {
            if(!this.level().isClientSide)
            {
                Entity entity = ((ServerLevel)this.level()).getEntity(target);
                boolean flag = false;
                boolean flag1 = false;
                if(entity instanceof LivingEntity livingEntity)
                {
                    flag = true;
                    flag1 = livingEntity.isAlive();
                }
                if(flag)
                {
                    Optional<Entity> optional = this.getTrackTarget();
                    if(flag1)
                    {
                        if(optional.isEmpty() || !optional.get().getUUID().equals(target))
                        {
                            this.setTrackTarget(entity);
                        }
                    }
                    else
                    {
                        this.eraseTrackTarget();
                    }
                }
                else
                {
                    this.eraseTrackTarget();
                }
            }
            Optional<Entity> trackTarget = this.getTrackTarget();
            if(trackTarget.isPresent())
            {
                Entity entity = trackTarget.get();
                if(entity instanceof LivingEntity livingEntity)
                {
                    Vec3 initVec = this.position().vectorTo(livingEntity.position()).normalize().scale(2);
                    if(initVec.length() > 1)
                    {
                        this.setDeltaMovement(initVec);
                        this.updateRotation(ProjectileMovement.of(initVec).rot());
                    }
                }
            }
        }
        else if(this.getOwner() != null)
        {
            var entities = this.level().getEntities(this, this.getBoundingBox().inflate(4));
            for (Entity tar : entities) {
                if(setTarget(tar))
                {
                    break;
                }
            }
        }
    }
    public boolean setTarget(Entity target) {
        if (target instanceof LivingEntity lv && canHitEntity(lv) && (!DamageSources.isFriendlyFireBetween(lv, this.getOwner()))) {
            this.target = lv.getUUID();
            return true;
        }
        return false;
    }
}
