package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class IdliberationModifier extends Modifier implements AttributesModifierHook, InventoryTickModifierHook, EquipmentChangeModifierHook, ModifierRemovalHook {
    final String unique = ModifierIds.idliberation.getNamespace()+  ".modifier."+ModifierIds.idliberation.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.INVENTORY_TICK, ModifierHooks.ATTRIBUTES);
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        if(equipmentSlot == EquipmentSlot.OFFHAND && iToolStackView.hasTag(TinkerTags.Items.BOWS)) {
            UUID uuid = this.getUUID(equipmentSlot);
            int level = modifierEntry.getLevel();
            boolean flag = iToolStackView.getPersistentData().getBoolean(ModifierIds.idliberation);
            if(flag) {
                level *= 2;
            }
            biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, this.getAttributeModifiername(equipmentSlot), level * 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            biConsumer.accept(ALObjects.Attributes.ARROW_DAMAGE.get(), new AttributeModifier(uuid, this.getAttributeModifiername(equipmentSlot), level * 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    private String getAttributeModifiername(EquipmentSlot equipmentSlot)
    {
        return this.unique + "." + equipmentSlot.getName();
    }

    private UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide)return;
        updatevalue(iToolStackView, isCorrectSlot && livingEntity.hasEffect(YHEffects.UNCONSCIOUS.get()));
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if(context.getLevel().isClientSide)return;
        updatevalue(tool,context.getEntity().hasEffect(YHEffects.UNCONSCIOUS.get()));
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if(context.getLevel().isClientSide)return;
        updatevalue(tool,false);
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(ModifierIds.idliberation);
        return null;
    }

    void updatevalue(IToolStackView tool, boolean flag1)
    {
        boolean flag = tool.getPersistentData().getBoolean(ModifierIds.idliberation);
        if(flag != flag1)
        {
            tool.getPersistentData().putBoolean(ModifierIds.idliberation, flag1);
        }
    }
}
