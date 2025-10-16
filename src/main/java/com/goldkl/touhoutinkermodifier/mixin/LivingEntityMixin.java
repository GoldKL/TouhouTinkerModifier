package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.api.LivingEntityCanStopTime;
import com.goldkl.touhoutinkermodifier.api.LivingEntityInWorldEnder;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityCanStopTime, LivingEntityInWorldEnder {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    @Unique
    private static EntityDataAccessor<Integer> DATA_TIME_STOP;
    @Unique
    private static EntityDataAccessor<Boolean> DATA_HAS_WORLD_ENDER;
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        DATA_TIME_STOP = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        DATA_HAS_WORLD_ENDER = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    }
    @Shadow
    public abstract boolean hasEffect(MobEffect p_21024_) ;
    @Shadow
    public abstract MobEffectInstance getEffect(MobEffect p_21125_);
    @Inject(method = "defineSynchedData",at = @At("TAIL"))
    void definenewdata(CallbackInfo ci)
    {
        this.entityData.define(DATA_TIME_STOP, -1);
        this.entityData.define(DATA_HAS_WORLD_ENDER, false);
    }
    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    void addnewdata(CompoundTag p_21145_, CallbackInfo ci)
    {
        p_21145_.putInt("TimeStop", this.entityData.get(DATA_TIME_STOP));
    }
    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    void readnewdata(CompoundTag p_21145_, CallbackInfo ci)
    {
        this.entityData.set(DATA_TIME_STOP,p_21145_.getInt("TimeStop"));
    }
    @Inject(method = "tickEffects",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateInvisibilityStatus()V"))
    void tickeffectupdatanewdata(CallbackInfo ci)
    {
        this.touhouTinkerModifier$updatetimestop();
        this.touhouTinkerModifier$updateWorldenderStatus();
    }
    @Inject(method = "sendEffectToPassengers",at = @At("HEAD"))
    void updatatimestopmixin(CallbackInfo ci)
    {
        this.touhouTinkerModifier$updatetimestop();
        this.touhouTinkerModifier$updateWorldenderStatus();
    }
    @Inject(method = "onEffectRemoved",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"))
    void onEffectRemovedMixin(MobEffectInstance p_21126_, CallbackInfo ci)
    {
        this.touhouTinkerModifier$updatetimestop();
        this.touhouTinkerModifier$updateWorldenderStatus();
    }
    @Unique
    void touhouTinkerModifier$updatetimestop()
    {
        boolean flag = this.hasEffect(MobeffectRegistry.TIMESTOP.get());
        if(flag)
        {
            int num = this.getEffect(MobeffectRegistry.TIMESTOP.get()).getAmplifier();
            if(this.entityData.get(DATA_TIME_STOP) != num)
                this.entityData.set(DATA_TIME_STOP, num);
        }
        else if(this.entityData.get(DATA_TIME_STOP) != -1) {
            this.entityData.set(DATA_TIME_STOP, -1);
        }
    }
    @Unique
    void touhouTinkerModifier$updateWorldenderStatus() {
        boolean flag = this.hasEffect(MobeffectRegistry.WORLDENDER.get());
        if (this.entityData.get(DATA_HAS_WORLD_ENDER) != flag) {
            this.entityData.set(DATA_HAS_WORLD_ENDER, flag);
        }
    }
    @Unique
    @Override
    public boolean touhouTinkerModifier$isCurrentlyWorldender() {
        return this.level().isClientSide()?this.entityData.get(DATA_HAS_WORLD_ENDER):this.hasEffect(MobeffectRegistry.WORLDENDER.get());
    }
    @Unique
    @Override
    public int touhouTinkerModifier$isCanStopTime(){
        return this.entityData.get(DATA_TIME_STOP);
    }
    @Inject(method = "getJumpBoostPower",at = @At("RETURN"), cancellable = true)
    protected void getJumpBoostPowermixin(CallbackInfoReturnable<Float> cir) {
        if(this.touhouTinkerModifier$isCurrentlyWorldender())
        {
            cir.setReturnValue(cir.getReturnValue() + 0.1f);
        }
    }

    @WrapOperation(method = "updateFallFlying",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"))
    void updateFallFlyingmixin(LivingEntity instance, int index, boolean bool, Operation<Void> original) {
        boolean res = bool;
        if (!res) {
            boolean flag = this.getSharedFlag(7);
            if (flag && !this.onGround() && !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION)) {
                res = this.touhouTinkerModifier$isCurrentlyWorldender();
            }
        }
        original.call(instance, index, res);
    }
}
