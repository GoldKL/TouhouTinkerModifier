package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class YoungscarletmoonModifier extends Modifier implements ModifyDamageModifierHook {
    //鲜红幼月：蕾米莉亚
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.youngscarletmoon);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
        if(level > 0)
        {
            LivingEntity entity = context.getEntity();
            double SpellPowerModifier = entity.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            double SchoolPowerModifier = entity.getAttributeValue(AttributeRegistry.BLOOD_SPELL_POWER.get());
            double isBaotoudunfang = entity.isCrouching()&&entity.getXRot()<0 ? 2 : 1;
            amount *= (float) (getnum(4 * isBaotoudunfang * SpellPowerModifier * level * SchoolPowerModifier));
        }
        return amount;
    }
    private double getnum(double x)
    {
        x = Math.max(-4.0,x);
        return 1.0-x/(x+8.0);
    }

}
