package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.BetterCombatAtackRangeHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;


import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JacktheripperModifier extends NoLevelsModifier implements BetterCombatAtackRangeHook, ToolStatsModifierHook, AttributesModifierHook {
    //开膛杰克：十六夜咲夜
    final String unique = TTMModifierIds.jacktheripper.getNamespace()+  ".modifier."+ TTMModifierIds.jacktheripper.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACK_RANGE_MUL, ModifierHooks.TOOL_STATS,ModifierHooks.ATTRIBUTES);
    }
    @Override
    public double attackermodifyBetterCombatAtackRange(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean IsAdd, double amount, double tool_attack_range, InteractionHand hand) {
        if(slotType == EquipmentSlot.MAINHAND && hand == InteractionHand.MAIN_HAND || slotType == EquipmentSlot.OFFHAND &&  hand == InteractionHand.OFF_HAND) {
            if(tool.hasTag(TagsRegistry.ItemsTag.DAGGER))
            {
                amount *= 2;
            }
        }
        return amount;
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (context.hasTag(TinkerTags.Items.MELEE_WEAPON) && context.hasTag(TagsRegistry.ItemsTag.DAGGER)) {
            ToolStats.ATTACK_SPEED.multiply(builder, 2.0);
        }
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer)
    {
        if(iToolStackView.hasTag(TagsRegistry.ItemsTag.DAGGER))
        {
            UUID uuid = this.getUUID(slot);
            if(uuid != null)
            {
                AttributeModifier attributeModifier = new AttributeModifier(uuid, this.unique + "." + slot.getName(), 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                biConsumer.accept(ForgeMod.ENTITY_REACH.get(), attributeModifier);
            }
        }
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }

}
