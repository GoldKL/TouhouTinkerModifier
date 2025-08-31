package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WelldestructorModifier extends Modifier implements MeleeDamagePercentModifierHook {
    //瓶落之时：琪斯美
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity attacker = context.getAttacker();
        float level = modifier.intEffectiveLevel();
        if(attacker.fallDistance >= 1.5)
        {
            float percent;
            if(attacker.fallDistance <= 3)
            {
                percent = attacker.fallDistance / 4f;
            }
            else if(attacker.fallDistance <= 8)
            {
                percent = 0.75f + (attacker.fallDistance - 3) / 8f;
            }
            else
            {
                percent = 1.375f + (attacker.fallDistance - 8) / 16f;
            }
            percent +=(level - 1)/32f;
            damagemodifier.addMultiply(1f + percent);
            attacker.fallDistance = 0;
        }
    }
}
