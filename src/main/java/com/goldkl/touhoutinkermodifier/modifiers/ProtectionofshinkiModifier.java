package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;

public class ProtectionofshinkiModifier extends Modifier implements InventoryTickModifierHook, ModifierRemovalHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule
                .builder(AttributesRegistry.BOUNDARY_POWER.get(), AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> iToolStackView.getPersistentData().getBoolean(TTMModifierIds.protectionofshinki)))
                .uniqueFrom(TTMModifierIds.protectionofshinki)
                .eachLevel(10f));
        hookBuilder.addModule(AttributeModule
                .builder(PerkAttributes.MANA_REGEN_BONUS.get(), AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.protectionofshinki)))
                .uniqueFrom(TTMModifierIds.protectionofshinki)
                .eachLevel(10f));
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.REMOVE);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide)return;
        updatevalue(iToolStackView, livingEntity.level().dimension().equals(TFGenerationSettings.DIMENSION_KEY));
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(TTMModifierIds.protectionofshinki);
        return null;
    }

    void updatevalue(IToolStackView tool, boolean flag1)
    {
        boolean flag = tool.getPersistentData().getBoolean(TTMModifierIds.protectionofshinki);
        if(flag != flag1)
        {
            tool.getPersistentData().putBoolean(TTMModifierIds.protectionofshinki, flag1);
        }
    }
}
