package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityDodgeHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


public class PerfectandelegantModifier extends Modifier implements EntityDodgeHook, InventoryTickModifierHook, TooltipModifierHook , EquipmentChangeModifierHook {
    //完美潇洒：十六夜咲夜
    final String unique = TTMModifierIds.perfectandelegant.getNamespace()+  ".modifier."+ TTMModifierIds.perfectandelegant.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(ALObjects.Attributes.DODGE_CHANCE.get(),Attributes.ARMOR);
    private static final List<Float> attributes_amount = List.of(0.0625f,6.0f);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_DODGE_HOOK, ModifierHooks.INVENTORY_TICK,ModifierHooks.TOOLTIP, ModifierHooks.EQUIPMENT_CHANGE);
    }
    @Override
    public void OnDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, @Nullable Entity attacker, @Nullable Entity directattacker){
        if(attacker instanceof LivingEntity livingEntity)
        {
            int level = modifier.getLevel();
            livingEntity.addEffect(new MobEffectInstance(MobeffectRegistry.IMPRISON.get(), 60, 0, false, true),context.getEntity());
            livingEntity.addEffect(new MobEffectInstance(MobeffectRegistry.FRAGILE.get(), 60, level - 1, false, true),context.getEntity());
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        if(TTMEntityUtils.validArmorTool(tool,isCorrectSlot,entity,itemStack))
        {
            EquipmentSlot slot = null;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(entity.getItemBySlot(equipmentSlot) == itemStack)
                {
                    slot = equipmentSlot;
                    break;
                }
            }
            if(slot != null)
            {
                int level = modifier.getLevel();
                for(int i = 0; i < 2; i++) {
                    AttributeInstance instance = entity.getAttribute(attributes.get(i));
                    if(instance != null)
                    {
                        AttributeModifier attributeModifier = this.createModifier(tool, modifier, slot, level,i);
                        if (attributeModifier != null) {
                            instance.removeModifier(attributeModifier);
                            if(Float.compare(entity.getMaxHealth(),entity.getHealth()) == 0)
                            {
                                instance.addTransientModifier(attributeModifier);
                            }
                        }
                    }
                }
            }
        }
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, int level, int index) {
        UUID uuid = this.getUUID(slot);
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), level * attributes_amount.get(index), AttributeModifier.Operation.ADDITION) : null;
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int level = modifier.getLevel();
        list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.perfectandelegant.fullhealth")));
        TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(attributes.get(0).getDescriptionId()), level * attributes_amount.get(0), list);
        TooltipModifierHook.addFlatBoost(modifier.getModifier(), Component.translatable(attributes.get(1).getDescriptionId()), level * attributes_amount.get(1), list);
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        UUID uuid = this.getUUID(context.getChangedSlot());
        if(uuid != null)
        {
            for(int i = 0; i < 2; i++) {
                AttributeInstance instance = context.getEntity().getAttribute(attributes.get(i));
                if(instance != null)
                {
                    instance.removeModifier(uuid);
                }
            }
        }
    }
}
