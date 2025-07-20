package com.goldkl.touhoutinkermodifier.mixin;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.api.LevelhaveLivingEntityTimeStop;
import com.goldkl.touhoutinkermodifier.api.LivingEntityCanStopTime;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
import com.goldkl.touhoutinkermodifier.mobeffect.TimestopEffect;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.google.common.collect.Lists;
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
        boolean flag1 = false;
        if (entity instanceof LivingEntity lventity) {
            if(((LivingEntityCanStopTime)lventity).touhouTinkerModifier$isCanStopTime() > -1)
            {
                ((LevelhaveLivingEntityTimeStop)(Object)this).touhouTinkerModifier$getLivingEntitiesHavingTimeStop().add(lventity);
                flag1 = true;
            }
            else
            {
                if(lventity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                    flag1 = true;
                }
                ((LevelhaveLivingEntityTimeStop)(Object)this).touhouTinkerModifier$getLivingEntitiesHavingTimeStop().remove(lventity);
            }
        }
        if(!flag1)
        {
            boolean flag = true;
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
                ci.cancel();
            }
        }
    }
}
