package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
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
    //德州之夜：驹草山如
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.texasnight);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext context, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if(!(damageSource.is(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER)||damageSource.is(DamageTypes.STARVE)))
        {
            if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot))
            {
                LivingEntity entity = context.getEntity();
                int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot);
                AttributeInstance lucky = entity.getAttribute(Attributes.LUCK);
                double A = 1.0;
                double B = 0.0;
                if(lucky != null)
                {
                    double luck = lucky.getBaseValue()/50;
                    if(luck + level >= 0.5)
                    {
                        A = 0.0;
                        B = luck + level;
                    }
                    else
                    {
                        A = luck + level - 0.5;
                        B = 0.5;
                    }
                }
                double parameter = (2.0 - A) * Math.pow(RANDOM.nextDouble(), B);
                return (float)  (amount * parameter);
            }
        }
        return amount;
    }
}
