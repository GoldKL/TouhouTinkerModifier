package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityHealHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class KojisousamadesitaModifier extends Modifier implements EntityHealHook {
    //谢谢款待：EX露米娅
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.kojisousamadesita);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_HEAL_HOOK);
        hookBuilder.addModule(AttributeModule.builder(ALObjects.Attributes.LIFE_STEAL.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.kojisousamadesita).eachLevel(0.1f));
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    @Override
    public float modifyhealTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, float baseheal, float heal)
    {
        if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType))
        {
            LivingEntity entity = context.getEntity();
            if(entity.getHealth() < 0.4f * entity.getMaxHealth())
            {
                heal *= 2;
            }
        }
        return heal;
    }
}
