package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifierConfig;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HellcrowModifier extends NoLevelsModifier implements AttackerWithEquipmentModifyDamageModifierHook, ModifyDamageModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.hellcrow);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT, ModifierHooks.MODIFY_HURT);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        damageModifier.addPercent(0.5f);
    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType))
        {
            LivingEntity entity = context.getEntity();
            //if(entity.level().dimension() != Level.NETHER)
            if(!TouhouTinkerModifierConfig.hellcrow_dimension.contains(entity.level().dimension()))
            {
                TouhouTinkerModifier.LOGGER.info("test");
                int level = TTMEntityUtils.getModifiertotalLevel(entity, TTMModifierIds.hellcrow);
                amount *= 0.3f * level + 1;
            }
        }
        return amount;
    }
}
