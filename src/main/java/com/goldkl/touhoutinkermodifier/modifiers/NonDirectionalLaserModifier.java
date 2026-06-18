package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ProjectileDamageModifierHook;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.tools.modules.ranged.common.ArrowPierceModule;


public class NonDirectionalLaserModifier extends Modifier implements ProjectileDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PROJECTILE_DAMAGE);
        hookBuilder.addModule(new ArrowPierceModule(LevelingInt.eachLevel(1), ModifierCondition.ANY_TOOL));
    }
    @Override
    public float getProjectileDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers,@NotNull Projectile projectile,@Nullable AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        int level = entry.getLevel();
        float fixnum = 1f;
        if(target instanceof LivingEntity lv){
            if(!lv.hasEffect(MobEffects.GLOWING))
            {
                lv.addEffect(new MobEffectInstance(MobEffects.GLOWING,300,0));
            }
            else {
                lv.removeEffect(MobEffects.GLOWING);
                fixnum *= 1 + 0.05f * level;
                if(attacker != null){
                    int newlevel = -1;
                    if(attacker.hasEffect(MobeffectRegistry.ORRERYS_SOLAR_SYSTEM.get())){
                        newlevel = attacker.getEffect(MobeffectRegistry.ORRERYS_SOLAR_SYSTEM.get()).getAmplifier();
                    }
                    newlevel = Math.min(newlevel + 1, level * 2);
                    attacker.addEffect(new MobEffectInstance(MobeffectRegistry.ORRERYS_SOLAR_SYSTEM.get(),400,newlevel));
                }
            }
        }
        if(!persistentData.getBoolean(TTMModifierIds.nondirectionallaser))
        {
            persistentData.putBoolean(TTMModifierIds.nondirectionallaser,true);
            fixnum *= 1 + 0.1f * level;
        }
        return damage * fixnum;
    }
}
