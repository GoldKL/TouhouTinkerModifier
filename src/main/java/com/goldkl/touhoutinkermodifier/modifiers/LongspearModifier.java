package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.BetterCombatAtackRangeHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class LongspearModifier extends NoLevelsModifier implements BetterCombatAtackRangeHook, AttributesModifierHook {
    final String unique = TTMModifierIds.longspear.getNamespace()+  ".modifier."+ TTMModifierIds.longspear.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACK_RANGE_MUL,ModifierHooks.ATTRIBUTES);
    }
    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }
    @Override
    public double attackermodifyBetterCombatAtackRange(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean IsAdd, double amount, double tool_attack_range, InteractionHand hand) {
        if(slotType == EquipmentSlot.MAINHAND && hand == InteractionHand.MAIN_HAND || slotType == EquipmentSlot.OFFHAND && hand == InteractionHand.OFF_HAND) {
            amount *= 1.75;
        }
        return amount;
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer)
    {
        UUID uuid = this.getUUID(slot);
        if(uuid != null)
        {
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.unique + "." + slot.getName(), 0.75, AttributeModifier.Operation.MULTIPLY_TOTAL);
            biConsumer.accept(ForgeMod.ENTITY_REACH.get(), attributeModifier);
        }
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
}
