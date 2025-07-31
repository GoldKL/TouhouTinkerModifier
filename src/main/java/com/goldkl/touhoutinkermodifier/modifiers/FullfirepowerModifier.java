package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FullfirepowerModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {//MeleeDamageModifierHook{
    //火力全开：主角组
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.fullfirepower);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public float attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source,float baseamount, float amount, boolean isDirectDamage) {
        if(source.is(YHDamageTypes.DANMAKU_TYPE))
        {
            int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
            if(level > 0){
                return (float)  (amount * (1 + 0.2 * level));
            }
        }
        return amount;
    }
}
