package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.Iterator;

public class VanhelsingprogenyModifier extends Modifier implements MeleeDamagePercentModifierHook {//MeleeDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        //hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    /*@Override
    public float getMeleePercent(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float percent)
    {
        LivingEntity target = context.getLivingTarget();
        if(target != null) {
            boolean flag1 = target.getMobType() == MobType.UNDEAD;//判断是不是不死生物
            boolean flag2 = isSilver(tool);//判断有无特定词条或银材质
            if(flag1 || flag2) {
                int level = modifier.getLevel();
                percent += level * 0.1f;
            }
        }
        return percent;
    }*/
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity target = context.getLivingTarget();
        if(target != null) {
            boolean flag1 = target.getMobType() == MobType.UNDEAD;//判断是不是不死生物
            boolean flag2 = isSilver(tool);//判断有无特定词条或银材质
            if(flag1 || flag2) {
                int level = modifier.getLevel();
                damagemodifier.addPercent(level * 0.1f);
            }
        }
    }
    /*@Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        if(target != null) {
            boolean flag1 = target.getMobType() == MobType.UNDEAD;//判断是不是不死生物
            boolean flag2 = isSilver(tool);//判断有无特定词条或银材质
            if(flag1 || flag2) {
                int level = modifier.getLevel();
                damage += baseDamage * level * 0.1f;
            }
        }
        return damage;
    }*/

    private static boolean isSilver(IToolStackView tool)
    {
        if(tool.getModifiers().has(TagsRegistry.ModifiersTag.SilverModifier))
        {
            return true;
        }
        boolean flag = false;
        for (MaterialVariant material : tool.getMaterials()) {
            if (material.matchesVariant(MaterialIds.silver)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
