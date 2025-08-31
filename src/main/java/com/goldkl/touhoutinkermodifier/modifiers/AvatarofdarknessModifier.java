package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AvatarofdarknessModifier extends Modifier implements ModifyDamageModifierHook, InventoryTickModifierHook {
    //黑暗化身：EX露米娅
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.avatarofdarkness);
    public int getPriority() {
        return 15;
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
        hookBuilder.addHook(this,ModifierHooks.INVENTORY_TICK, ModifierHooks.MODIFY_HURT);
    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if(context.getEntity().hasEffect(MobeffectRegistry.BREAKDARKNESS.get()))return amount;
        if(damageSource.is(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER))return amount;
        int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
        if(level > 0)
        {
            LivingEntity entity = context.getEntity();
            float absorb = entity.getAbsorptionAmount();
            if(absorb > 0 && absorb < amount)
            {
                entity.addEffect(new MobEffectInstance(MobeffectRegistry.BREAKDARKNESS.get(),6000,0));
                return 0;
            }
        }
        return amount;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(livingEntity.hasEffect(MobeffectRegistry.BREAKDARKNESS.get()))return;
        if(!isCorrectSlot)return;
        EquipmentSlot slot = null;
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(livingEntity.getItemBySlot(equipmentSlot) == itemStack)
            {
                slot = equipmentSlot;
                break;
            }
        }
        if(slot!=null && SlotInChargeModule.isInCharge(livingEntity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot))
        {
            if(livingEntity.tickCount % 25 == 0)
            {
                int level = SlotInChargeModule.getLevel(livingEntity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot);
                int light = Math.max(world.getBrightness(LightLayer.BLOCK, livingEntity.blockPosition()), world.getBrightness(LightLayer.SKY, livingEntity.blockPosition()));
                if(light < 10)level *= 2;
                float absorb = level * 0.5f;
                if(absorb > 0.0f)
                    TTMEntityUtils.addLivingEntityAbsorptionAmountByMax(livingEntity, absorb);
            }
        }

    }
}
