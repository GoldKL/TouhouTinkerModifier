package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.tracking.DimensionCoefficientEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.json.predicate.tool.ToolContextPredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.armor.MaxArmorAttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.MaxArmorLevelModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TanabatazakamugennouModifier extends NoLevelsModifier implements ModifyDamageModifierHook, AttackerWithEquipmentModifyDamageModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.tanabatazakamugennou);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributesRegistry.BOUNDARY_POWER.get(), AttributeModifier.Operation.MULTIPLY_TOTAL)
                .toolContext(ToolContextPredicate.simple(SecretsealingclubModifier::SecretSealingClubcanUse))
                .uniqueFrom(TTMModifierIds.tanabatazakamugennou)
                .flat(0.06f));
        /*hookBuilder.addModule(MaxArmorAttributeModule.builder(AttributesRegistry.BOUNDARY_POWER.get(), AttributeModifier.Operation.MULTIPLY_TOTAL)
                .uniqueFrom(TTMModifierIds.tanabatazakamugennou)
                .toolContext(ToolContextPredicate.simple(SecretsealingclubModifier::SecretSealingClubcanUse))
                .flat(0.1f));*/
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));

    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType))
        {
            if(!SecretsealingclubModifier.SecretSealingClubcanUse(tool))
            {
                amount *= 1.2f;
            }
            context.getEntity().invulnerableTime += 20;
        }
        return amount;
    }
    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType)){
            if(DimensionCoefficientEvent.getEntityBoundaryPower(context.getEntity()) > DimensionCoefficientEvent.getEntityBoundaryPower(target))
            {
                damageModifier.addMultiply(1.3f);
            }
        }
    }
}
