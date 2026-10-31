package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
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
import slimeknights.tconstruct.library.modifiers.modules.armor.ProtectionModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class PasterdreamArmorModifier extends Modifier implements InventoryTickModifierHook, ModifierRemovalHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(ProtectionModule.builder()
                .tool(ToolStackPredicate.simple( iToolStackView -> iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_armor)))
                .eachLevel(1.25f));
        hookBuilder.addModule(AttributeModule
                .builder(AttributeRegistry.SPELL_RESIST, AttributeModifier.Operation.MULTIPLY_BASE)
                .tool(ToolStackPredicate.simple( iToolStackView -> iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_armor)))
                .uniqueFrom(TTMModifierIds.pasterdream_armor)
                .eachLevel(0.08f));
        hookBuilder.addModule(AttributeModule
                .builder(AttributesRegistry.BOUNDARY_POWER, AttributeModifier.Operation.ADDITION)
                .tool(ToolStackPredicate.simple( iToolStackView -> !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_armor)))
                .uniqueFrom(TTMModifierIds.pasterdream_armor)
                .eachLevel(10));
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.REMOVE);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide)return;
        updatevalue(iToolStackView, livingEntity.level().dimension().registry().getNamespace().equals("pasterdream"));
        if(livingEntity.tickCount % 20 == 0 && !iToolStackView.getPersistentData().getBoolean(TTMModifierIds.pasterdream_armor)){
            livingEntity.heal(livingEntity.getMaxHealth() * 0.01f*modifierEntry.getLevel());
        }
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(TTMModifierIds.pasterdream_armor);
        return null;
    }

    void updatevalue(IToolStackView tool, boolean flag1)
    {
        boolean flag = tool.getPersistentData().getBoolean(TTMModifierIds.pasterdream_armor);
        if(flag != flag1)
        {
            tool.getPersistentData().putBoolean(TTMModifierIds.pasterdream_armor, flag1);
        }
    }
}
