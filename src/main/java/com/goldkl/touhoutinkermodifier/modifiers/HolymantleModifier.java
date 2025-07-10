package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;

import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.json.math.ModifierFormula;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HolymantleModifier extends Modifier implements InventoryTickModifierHook, DamageBlockModifierHook, EquipmentChangeModifierHook  {
    //神圣屏障：西行寺幽幽子
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.holymantle);
    final SlotInChargeModule SICM;
    public HolymantleModifier() {
        SICM = new SlotInChargeModule(SLOT_IN_CHARGE);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.DAMAGE_BLOCK,ModifierHooks.EQUIPMENT_CHANGE);
        hookBuilder.addModule(AttributeModule.builder(Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_TOTAL).uniqueFrom(ModifierIds.holymantle).formula().constant(0.8f).variable(ModifierFormula.LEVEL).power().constant(1.0f).subtract().build());
    }
    @Override
    public boolean isDamageBlocked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v) {
        if((!iToolStackView.isBroken())&&ModifierUtil.validArmorSlot(iToolStackView, equipmentSlot)&&SlotInChargeModule.isInCharge(equipmentContext.getTinkerData(), SLOT_IN_CHARGE,equipmentSlot))
        {
            if(DamageSourcePredicate.CAN_PROTECT.matches(damageSource))//!(damageSource.is(DamageTypes.GENERIC_KILL)||damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)||damageSource.is(DamageTypes.OUTSIDE_BORDER)||damageSource.is(DamageTypes.STARVE)))
            {
                LivingEntity entity = equipmentContext.getEntity();
                if(!entity.level().isClientSide)
                {
                    int count = entity.hasEffect(MobeffectRegistry.HOLYMANTLE.get()) ? entity.getEffect(MobeffectRegistry.HOLYMANTLE.get()).getAmplifier() : -1;
                    if(count > -1)
                    {
                        count--;
                        entity.removeEffect(MobeffectRegistry.HOLYMANTLE.get());
                        if(count > -1)
                            entity.addEffect(new MobEffectInstance(MobeffectRegistry.HOLYMANTLE.get(), -1, count, true, true));
                        return true;
                    }
                }
            }
        }
        return false;
        //指令杀，虚空伤害，世界外伤害，饥饿伤害以外的伤害免疫
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(!world.isClientSide)
        {
            if(isCorrectSlot)
            {
                EquipmentSlot slot = null;
                for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
                {
                    if(livingEntity.getItemBySlot(equipmentSlot) == itemStack)
                    {
                        slot = equipmentSlot;
                        break;
                    }
                }
                if(slot!=null)
                {
                    if(SlotInChargeModule.isInCharge(livingEntity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot))
                    {
                        int count = livingEntity.hasEffect(MobeffectRegistry.HOLYMANTLE.get()) ? livingEntity.getEffect(MobeffectRegistry.HOLYMANTLE.get()).getAmplifier() : -1;
                        int level = SlotInChargeModule.getLevel(livingEntity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot)-1;
                        boolean changed = false;
                        if(count > level)
                        {
                            count = level;
                            changed = true;
                        }
                        else if(count < level && livingEntity.tickCount%600 == 0)
                        {
                            count ++;
                            changed = true;
                        }
                        if(changed)
                        {
                            livingEntity.removeEffect(MobeffectRegistry.HOLYMANTLE.get());
                            if(count > -1)
                                livingEntity.addEffect(new MobEffectInstance(MobeffectRegistry.HOLYMANTLE.get(), -1, count, true, true));
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context)
    {
        SICM.onEquip(tool, modifier, context);
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        SICM.onUnequip(tool, modifier, context);
        if(!context.getEntity().level().isClientSide)
        {
            boolean check = false;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot))
                {
                    check = true;
                    break;
                }
            }
            if(!check)
            {
                LivingEntity entity = context.getEntity();
                entity.removeEffect(MobeffectRegistry.HOLYMANTLE.get());
            }
        }
    }
}
