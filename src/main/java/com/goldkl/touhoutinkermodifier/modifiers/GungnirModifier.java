package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CriticalAttackModifierHook;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Optional;

public class GungnirModifier extends Modifier implements CriticalAttackModifierHook, MeleeHitModifierHook {
    //冈格尼尔：蕾米莉亚·斯卡雷特
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.MELEE_HIT, EtSTLibHooks.CRITICAL_ATTACK);
    }
    @Override
    public boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical) {
        if(tool.hasTag(TagsRegistry.ItemsTag.SPEAR))
        {
            return true;
        }
        return isCritical;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        if(tool.hasTag(TagsRegistry.ItemsTag.SPEAR)&&!context.isExtraAttack()) {
            LivingEntity attacker = context.getAttacker();
            LivingEntity target = context.getLivingTarget();
            if(target != null)
            {
                int level = modifier.getLevel();
                TTMEntityUtils.clearLivingEntityInvulnerableTime(target);
                DamageSources.applyDamage(target, SpellsRegistry.gungnir.get().getSpellPower(level,attacker), SpellsRegistry.gungnir.get().getDamageSource(attacker));
            }
        }
    }
}
