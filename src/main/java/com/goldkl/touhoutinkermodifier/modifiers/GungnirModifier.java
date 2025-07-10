package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.mixininterface.ToolAttackContextMixinInterface;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
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

public class GungnirModifier extends Modifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    //冈格尼尔：蕾米莉亚·斯卡雷特
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.MELEE_HIT);
    }
    //尽可能上调优先级来保证暴击词条能吃到加成
    public int getPriority() {
        return 200;
    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if(tool.hasTag(TagsRegistry.ItemsTag.SPEAR)&&!context.isExtraAttack())
        {
            ((ToolAttackContextMixinInterface)context).touhouTinkerModifier$setCritical(true);
        }
        return damage;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        if(tool.hasTag(TagsRegistry.ItemsTag.SPEAR)&&!context.isExtraAttack()) {
            LivingEntity attacker = context.getAttacker();
            LivingEntity target = context.getLivingTarget();
            double entitySpellPowerModifier = 1;
            double entitySchoolPowerModifier = 1;
            float configPowerModifier = 1;
            float baseSpellPower = 12;
            float spellPowerPerLevel = 2;
            entitySpellPowerModifier = (float) attacker.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = (float) attacker.getAttributeValue(AttributeRegistry.BLOOD_SPELL_POWER.get());
            int level = modifier.getLevel();
            float basedamage = (float) ((baseSpellPower + spellPowerPerLevel * (level - 1)) * entitySpellPowerModifier * entitySchoolPowerModifier * configPowerModifier);
            if (target != null) {
                float finaldamage = basedamage * DamageSources.getResist(target, SchoolRegistry.BLOOD.get());
                Optional<Holder.Reference<DamageType>> option = attacker.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DamageTypes.MAGIC);
                Holder<DamageType> holder = option.isPresent() ? (Holder) option.get() : attacker.level().damageSources().genericKill().typeHolder();
                target.invulnerableTime = 0;
                target.hurt(new DamageSource(holder, attacker), finaldamage);
            }
        }
    }
}
