package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.communication.FireworkMessage;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.tracking.ChannelEventTracker;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class AndthentherewerenoneModifier extends Modifier implements ProjectileHitModifierHook, ProjectileLaunchModifierHook {
    //无人生还：芙兰朵路
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.PROJECTILE_LAUNCH);
    }
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if(arrow != null && arrow.isCritArrow() && primary) {
            persistentData.putBoolean(TTMModifierIds.andthentherewerenone,true);
        }
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if(persistentData.getBoolean(TTMModifierIds.andthentherewerenone))
            explode(projectile,modifier.getLevel());
        return false;
    }
    @Override
    public void onProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, BlockHitResult hit, @Nullable LivingEntity attacker) {
        if(persistentData.getBoolean(TTMModifierIds.andthentherewerenone))
            explode(projectile,modifier.getLevel());
    }
    void explode(Projectile projectile,int level) {
        if(!projectile.level().isClientSide()){
            projectile.level().broadcastEntityEvent(projectile, (byte)17);
            projectile.gameEvent(GameEvent.EXPLODE, projectile.getOwner());
            {
                CompoundTag fireworkNBT = new CompoundTag();
                ListTag explosions = new ListTag();
                // 添加一个爆炸效果（红色+闪烁）
                CompoundTag explosion1 = new CompoundTag();
                explosion1.putIntArray("Colors", new int[]{0x8b0000});
                explosion1.putBoolean("Flicker", true);
                explosion1.putByte("Type", (byte) 4);
                explosions.add(explosion1);
                fireworkNBT.put("Explosions", explosions);
                ChannelEventTracker.sendToAllPlayers(new FireworkMessage(projectile.getX(),projectile.getY(),projectile.getZ(),0,0,0,fireworkNBT));
            }
            double d0 = 5 * (level + 1);
            float f = 40 + 10 * level;
            Vec3 vec3 = projectile.position();

            for(LivingEntity livingentity : projectile.level().getEntitiesOfClass(LivingEntity.class, projectile.getBoundingBox().inflate(d0))) {
                if (livingentity != projectile.getOwner() && !(projectile.distanceToSqr(livingentity) > d0*d0)) {
                    float f1 = f * (float)Math.sqrt((d0 - (double)projectile.distanceTo(livingentity)) / d0);
                    livingentity.hurt(new DamageSource(projectile.level().damageSources().outOfBorder().typeHolder(), projectile.getOwner()), f1);
                    TTMEntityUtils.clearLivingEntityInvulnerableTime(livingentity);
                }
            }
        }
    }
}
