package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TexasnightModifier extends Modifier implements ModifyDamageModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.texasnight);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext context, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if(!(damageSource.is(DamageTypes.GENERIC_KILL)||damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)||damageSource.is(DamageTypes.OUTSIDE_BORDER)||damageSource.is(DamageTypes.STARVE)))
        {
            if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot))
            {
                LivingEntity entity = context.getEntity();
                int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot);
                AttributeInstance lucky = entity.getAttribute(Attributes.LUCK);
                double goodluck = 0.0;
                double badluck = 0.0;
                if(lucky != null)
                {
                    if(lucky.getValue() > 0.0)
                        goodluck = lucky.getValue();
                    else
                        badluck = -1.0 * lucky.getValue();
                }
                double truelevel = goodluck + level;
                double parameter = (2.0 + badluck) * Math.pow(RANDOM.nextDouble(), 1/truelevel);
                return (float)  (amount * parameter);
            }
        }
        return amount;
    }
}
