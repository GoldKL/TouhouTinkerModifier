package com.goldkl.touhoutinkermodifier.modifiers;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
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

public class LaevateinModifier extends Modifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    //莱瓦汀：芙兰朵露·斯卡雷特
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.MELEE_HIT);
    }
    //尽可能下降优先级来保证增伤害吃满
    public int getPriority() {
        return 50;
    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if(tool.hasTag(TagsRegistry.ItemsTag.CLAYMORE)&&!context.isExtraAttack())
        {
            int level = modifier.getLevel();
            damage *= 1.0f + 0.5f*level;
        }
        return damage;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        if(tool.hasTag(TagsRegistry.ItemsTag.CLAYMORE)&&!context.isExtraAttack()) {
            LivingEntity attacker = context.getAttacker();
            LivingEntity target = context.getLivingTarget();
            if(target != null)
            {
                int level = modifier.getLevel();
                TTMEntityUtils.clearLivingEntityInvulnerableTime(target);
                DamageSources.applyDamage(target, SpellsRegistry.laevatein.get().getSpellPower(level,attacker), SpellsRegistry.laevatein.get().getDamageSource(attacker));
                target.setRemainingFireTicks(100 + level*60);
            }
        }
    }
}
