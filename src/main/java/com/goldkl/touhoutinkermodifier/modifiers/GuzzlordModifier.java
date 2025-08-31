package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GuzzlordModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {
    //恶食大王：西行寺幽幽子
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.guzzlord);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier,LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source,float baseamount,  DamageModifier damageModifier, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if(entity instanceof Player player) {
            if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType)){
                int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
                FoodData foodData = player.getFoodData();
                double hungry = 20 - Math.max(foodData.getFoodLevel() , 8);//0 ~ 12
                double parameter = 0.2 * level * hungry / 12.0;
                damageModifier.addPercent((float) parameter);
            }
        }
        else
        {
            if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType)){
                int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
                double scale = Math.min((entity.getMaxHealth()-entity.getHealth())/entity.getMaxHealth(),0.6);
                double parameter = 0.2 * level * scale / 0.6;
                damageModifier.addPercent((float) parameter);
            }
        }
    }
}
