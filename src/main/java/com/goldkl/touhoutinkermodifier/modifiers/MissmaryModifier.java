package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vectorwing.farmersdelight.common.item.enchantment.BackstabbingEnchantment;

public class MissmaryModifier extends Modifier implements MeleeDamagePercentModifierHook {
    //玛丽小姐：古明地恋
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();
        if(target != null && BackstabbingEnchantment.isLookingBehindTarget(target,attacker.position()))
        {
            int level = modifier.getLevel();
            boolean flag = attacker.hasEffect(YHEffects.UNCONSCIOUS.get());
            damagemodifier.addPercent(level * 0.2f);
            damagemodifier.addFixed(target.getMaxHealth() * 0.05f);
            if(flag)
            {
                damagemodifier.addMultiply(2f);
            }
        }
    }
}
