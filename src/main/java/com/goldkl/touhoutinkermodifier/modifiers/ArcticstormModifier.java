package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.spells.ice.ArcticstormSpell;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ArcticstormModifier extends Modifier implements MeleeHitModifierHook {
    //冻原风暴：⑨或蕾蒂
    TinkerDataCapability.ComputableDataKey<HitCount>hitcount = TinkerDataCapability.ComputableDataKey.of(ModifierIds.arcticstorm.withSuffix("_data"), HitCount::new);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        int level = modifier.getLevel() - 1;
        LivingEntity attacker = context.getAttacker();
        MobEffectInstance instance = attacker.getEffect(MobeffectRegistry.ARCTICSTROM.get());
        final boolean[] flag = {false};
        if(instance == null || instance.getAmplifier() < level)
        {
            attacker.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                HitCount hitCount = data.computeIfAbsent(this.hitcount);
                hitCount.update(attacker);
                if(hitCount.getCount() >= 3)
                {
                    flag[0] = true;
                }
            });
        }
        else if(instance.getAmplifier() == level)
        {
            flag[0] = true;
        }
        if(flag[0])
        {
            int effecttick = ((ArcticstormSpell)SpellsRegistry.arcticstorm.get()).getSpelltick(level + 1, attacker);
            if(instance == null || instance.getDuration() < effecttick)
            {
                attacker.addEffect(new MobEffectInstance(MobeffectRegistry.ARCTICSTROM.get(), effecttick , level, false, false, true));
            }
        }
    }
    private static class HitCount{
        private int count;
        private int lasttick;
        HitCount() {
            count = 0;
            lasttick = 0;
        }
        void update(LivingEntity livingEntity){
            if(livingEntity.tickCount - lasttick > 60){
                count = 1;
            }
            else {
                count ++;
            }
            lasttick = livingEntity.tickCount;
        }
        int getCount(){
            return count;
        }
    }
}
