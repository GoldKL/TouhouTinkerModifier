package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FullfirepowerModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {//MeleeDamageModifierHook{
    //火力全开：主角组
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
    }
    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier,LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source,float baseamount,  DamageModifier damageModifier, boolean isDirectDamage) {
        if(source.is(YHDamageTypes.DANMAKU_TYPE))
        {
            int level = modifier.getLevel();
            if(level > 0){
                damageModifier.addPercent(0.2f * level);
            }
        }
    }
}
