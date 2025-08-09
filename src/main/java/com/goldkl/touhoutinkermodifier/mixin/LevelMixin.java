package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.api.LevelhaveLivingEntityTimeStop;
import com.goldkl.touhoutinkermodifier.api.LivingEntityCanStopTime;
import com.goldkl.touhoutinkermodifier.mobeffect.TimestopEffect;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(Level.class)
public class LevelMixin implements LevelhaveLivingEntityTimeStop {
    @Unique
    private Set<LivingEntity> touhouTinkerModifier$livingentityhaveTimeStopList;
    @Unique
    @Override
    public Set<LivingEntity> touhouTinkerModifier$getLivingEntitiesHavingTimeStop() {
        return touhouTinkerModifier$livingentityhaveTimeStopList;
    }
    @Inject(method = "<init>", at = @At("TAIL"))
    void init(CallbackInfo ci) {
        touhouTinkerModifier$livingentityhaveTimeStopList = Sets.newHashSet();
    }
    @Inject(method = "guardEntityTick",at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void test(Consumer<T> consumer, T entity, CallbackInfo ci)
    {
        boolean flag1 = false;//是否无视时停
        this.touhouTinkerModifier$updataTimeStop(entity);
        if (entity instanceof LivingEntity lventity) {
            if(((LivingEntityCanStopTime)lventity).touhouTinkerModifier$isCanStopTime() > -1)
            {
                flag1 = true;
            }
            else
            {
                if(lventity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                    flag1 = true;
                }
            }
        }
        if(!flag1)
        {
            boolean flag = true;//是否没有被时停
            for(LivingEntity livingEntity:((LevelhaveLivingEntityTimeStop)(Object)this).touhouTinkerModifier$getLivingEntitiesHavingTimeStop())
            {
                if(!livingEntity.isRemoved())
                {
                    int level = ((LivingEntityCanStopTime)livingEntity).touhouTinkerModifier$isCanStopTime() + 1;
                    if(livingEntity.distanceTo(entity) <= level * TimestopEffect.distance)
                    {
                        flag = false;
                        break;
                    }
                }
            }
            if(!flag)
            {
                if(entity instanceof LivingEntity livingEntity)
                {
                    TTMEntityUtils.clearLivingEntityInvulnerableTime(livingEntity);
                }
                for(Entity rider:entity.getPassengers())
                {
                    rider.stopRiding();
                }
                ci.cancel();
            }
        }
    }
    @Unique
    void touhouTinkerModifier$updataTimeStop(Entity entity)
    {
        if (entity instanceof LivingEntity lventity) {
            if(((LivingEntityCanStopTime)lventity).touhouTinkerModifier$isCanStopTime() > -1)
            {
                ((LevelhaveLivingEntityTimeStop)(Object)this).touhouTinkerModifier$getLivingEntitiesHavingTimeStop().add(lventity);
            }
            else
            {
                ((LevelhaveLivingEntityTimeStop)(Object)this).touhouTinkerModifier$getLivingEntitiesHavingTimeStop().remove(lventity);
            }
        }
        for(Entity rider:entity.getPassengers())
        {
            this.touhouTinkerModifier$updataTimeStop(rider);
        }
    }
}
