package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DisplayNameModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UshinokokumairiModifier extends Modifier implements MeleeHitModifierHook {
    //丑时参拜：水桥帕露西
    private static final ResourceLocation USHINOKOKUMAIRI = ModifierIds.ushinokokumairi;
    private static final int EXTENDTICK = 100;
    private static final int BASETICK = 100;
    private static final List<MobEffect> mobEffectList
            = List.of(MobEffects.POISON,
            MobEffects.WEAKNESS,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.WITHER,
            MobEffectRegistry.REND.get(),
            MobEffectRegistry.GUIDING_BOLT.get());
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        int level = modifier.intEffectiveLevel() - 1;
        LivingEntity entity = context.getLivingTarget();
        if(entity != null)
        {
            for(MobEffectInstance effect : entity.getActiveEffects())
            {
                if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                {
                    entity.addEffect(new MobEffectInstance(effect.getEffect(),
                            effect.getDuration() + EXTENDTICK,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()));
                }
            }
            for(int i = 0 ; i < 7 ; ++i)
            {
                if(!entity.canBeAffected(new MobEffectInstance(mobEffectList.get(i), BASETICK, level, false, true))||entity.hasEffect(mobEffectList.get(i)))
                    continue;
                entity.addEffect(new MobEffectInstance(mobEffectList.get(i), BASETICK, level, false, true));
                break;
            }
        }
    }
}
