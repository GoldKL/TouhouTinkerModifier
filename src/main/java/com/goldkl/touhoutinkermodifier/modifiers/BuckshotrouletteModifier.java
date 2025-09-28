package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BuckshotrouletteModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {//MeleeDamageModifierHook{
    //恶魔轮盘：驹草山如
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.buckshotroulette);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier,LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source,float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType)){
            LivingEntity entity = context.getEntity();
            int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
            AttributeInstance lucky = entity.getAttribute(Attributes.LUCK);
            double truelevel = 1.0 + level;
            double damagep = 1.0;
            if(lucky != null)
            {
                double luck = lucky.getValue()/50;
                if(luck > 0.0) {
                    damagep = damagep / (luck + 1.0);
                }
                else{
                    damagep = damagep - luck;
                }
                double parameter = truelevel * Math.pow(RANDOM.nextDouble(), damagep);
                damageModifier.addMultiply((float) parameter);
            }
        }
    }
}
