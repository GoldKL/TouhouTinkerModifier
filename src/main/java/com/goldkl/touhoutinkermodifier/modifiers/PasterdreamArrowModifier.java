package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.json.predicate.tool.ToolStackPredicate;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class PasterdreamArrowModifier extends Modifier implements InventoryTickModifierHook, ModifierRemovalHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule
                .builder(ALObjects.Attributes.DRAW_SPEED.get(), AttributeModifier.Operation.MULTIPLY_BASE)
                .tool(ToolStackPredicate.simple( iToolStackView -> iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow)))
                .uniqueFrom(TTMModifierIds.pasterdream_arrow)
                .eachLevel(0.1f));
        hookBuilder.addModule(AttributeModule
                .builder(ALObjects.Attributes.ARROW_DAMAGE.get(), AttributeModifier.Operation.MULTIPLY_BASE)
                .tool(ToolStackPredicate.simple( iToolStackView -> iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow)))
                .uniqueFrom(TTMModifierIds.pasterdream_arrow)
                .eachLevel(0.08f));
        hookBuilder.addModule(AttributeModule
                .builder(ALObjects.Attributes.ARMOR_PIERCE.get(), AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow)))
                .uniqueFrom(TTMModifierIds.pasterdream_arrow)
                .eachLevel(8f));
        hookBuilder.addModule(AttributeModule
                .builder(ALObjects.Attributes.LIFE_STEAL.get(), AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow)))
                .uniqueFrom(TTMModifierIds.pasterdream_arrow)
                .eachLevel(0.03f));
        hookBuilder.addModule(AttributeModule
                .builder(ALObjects.Attributes.OVERHEAL.get(), AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow)))
                .uniqueFrom(TTMModifierIds.pasterdream_arrow)
                .eachLevel(0.03f));
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.REMOVE);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide)return;
        updatevalue(iToolStackView, livingEntity.level().dimension().registry().getNamespace().equals("pasterdream"));
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(TTMModifierIds.pasterdream_arrow);
        return null;
    }

    void updatevalue(IToolStackView tool, boolean flag1)
    {
        boolean flag = tool.getPersistentData().getBoolean(TTMModifierIds.pasterdream_arrow);
        if(flag != flag1)
        {
            tool.getPersistentData().putBoolean(TTMModifierIds.pasterdream_arrow, flag1);
        }
    }
}
