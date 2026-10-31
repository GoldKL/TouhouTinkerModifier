package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ArrowHitModifierHook;
import com.goldkl.touhoutinkermodifier.communication.FireworkMessage;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.tracking.ChannelEventTracker;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;

public class ThousandtrillionflareModifier extends Modifier implements ProjectileHitModifierHook, ArrowHitModifierHook {
    //千兆耀斑：灵乌路空
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, EtSTLibHooks.ARROW_HIT);
    }
    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry modifier, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        explode(arrow,attacker,modifier.getLevel());
    }
    @Override
    public void onProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, BlockHitResult hit, @Nullable LivingEntity attacker) {
        if(projectile instanceof AbstractArrow arrow){
            explode(arrow,attacker,modifier.getLevel());
        }
    }
    void explode(AbstractArrow arrow,LivingEntity attacker,int level) {
        if(!arrow.level().isClientSide()){
            arrow.level().broadcastEntityEvent(arrow, (byte)17);
            arrow.gameEvent(GameEvent.EXPLODE, arrow.getOwner());
            {
                CompoundTag fireworkNBT = new CompoundTag();
                ListTag explosions = new ListTag();
                // 添加一个爆炸效果（红色+闪烁）
                CompoundTag explosion1 = new CompoundTag();
                explosion1.putIntArray("Colors", new int[]{0xfbac13});
                explosion1.putBoolean("Flicker", true);
                explosion1.putByte("Type", (byte) 4);
                explosions.add(explosion1);
                fireworkNBT.put("Explosions", explosions);
                ChannelEventTracker.sendToAllPlayers(new FireworkMessage(arrow.getX(),arrow.getY(),arrow.getZ(),0,0,0,fireworkNBT));
            }
            double d0 = 6 * level;
            for(LivingEntity livingentity : arrow.level().getEntitiesOfClass(LivingEntity.class, arrow.getBoundingBox().inflate(d0))) {
                if (livingentity != arrow.getOwner() && !(arrow.distanceToSqr(livingentity) > d0*d0)) {
                    float f1 = (float)Math.pow((d0 - (double)arrow.distanceTo(livingentity)) / d0, 0.3);
                    TTMEntityUtils.clearLivingEntityInvulnerableTime(livingentity);
                    int damage = Mth.ceil(Mth.clamp(arrow.getDeltaMovement().length() * arrow.getBaseDamage(), 0.0D, Integer.MAX_VALUE));
                    int splevel = SpellsRegistry.thousandtrillionflare.get().getLevelFor(level,attacker);
                    DamageSources.applyDamage(livingentity, f1 * damage * SpellsRegistry.thousandtrillionflare.get().getSpellPower(splevel,attacker), SpellsRegistry.thousandtrillionflare.get().getDamageSource(arrow, attacker));
                    TTMEntityUtils.clearLivingEntityInvulnerableTime(livingentity);
                }
            }
        }
    }
}
