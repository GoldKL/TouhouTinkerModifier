package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityDodgeHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


public class PerfectandelegantModifier extends Modifier implements EntityDodgeHook, InventoryTickModifierHook, TooltipModifierHook {
    //完美潇洒：十六夜咲夜
    final String unique = ModifierIds.perfectandelegant.getNamespace()+  ".modifier."+ModifierIds.perfectandelegant.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(ALObjects.Attributes.DODGE_CHANCE.get(),Attributes.ARMOR);
    private static final List<Float> attributes_amount = List.of(0.0625f,6.0f);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_DODGE_HOOK, ModifierHooks.INVENTORY_TICK,ModifierHooks.TOOLTIP);
    }
    @Override
    public void OnDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, @Nullable Entity attacker, @Nullable Entity directattacker){
        if(attacker instanceof LivingEntity livingEntity)
        {
            int level = modifier.getLevel();
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 9, false, true));
            livingEntity.addEffect(new MobEffectInstance(MobeffectRegistry.FRAGILE.get(), 60, level - 1, false, true));
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        if(isCorrectSlot)
        {
            if(Float.compare(entity.getMaxHealth(),entity.getHealth()) != 0)
            {
                return;
            }
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
                            instance.addTransientModifier(attributeModifier);
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
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @org.jetbrains.annotations.Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int level = modifier.getLevel();
        list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.perfectandelegant.fullhealth")));
        for(int i = 0; i < 2; i++) {
            TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(attributes.get(i).getDescriptionId()), level * attributes_amount.get(i), list);
        }
    }
}
