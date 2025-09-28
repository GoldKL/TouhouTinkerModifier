package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.mixin.ironspell.SchoolTypeAccessor;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierRequirementsModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;
import java.util.UUID;

public class RlyehtextModifier extends NoLevelsModifier implements EquipmentChangeModifierHook, TooltipModifierHook {
    //凝视深渊：本居小铃
    final String unique = TTMModifierIds.rlyehtext.getNamespace()+  ".modifier."+ TTMModifierIds.rlyehtext.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.TOOLTIP,ModifierHooks.EQUIPMENT_CHANGE);
        hookBuilder.addModule(ModifierRequirementsModule.builder().requireModifier(TTMModifierIds.bibliophilia,1).modifierKey(TTMModifierIds.rlyehtext).build());
    }
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if(context.getEntity().level().isClientSide)return;
        if (tool.getModifier(TTMModifierIds.bibliophilia) != ModifierEntry.EMPTY) {
            int level = tool.getModifierLevel(TTMModifierIds.bibliophilia);
            for (SchoolType schoolType : SchoolRegistry.REGISTRY.get()){
                Attribute attribute = ((SchoolTypeAccessor)schoolType).getpowerAttribute().orElse(null);
                if(attribute != null)
                {
                    AttributeInstance instance = context.getEntity().getAttribute(attribute);
                    if (instance != null) {
                        AttributeModifier attributeModifier = null;// = this.createModifier(tool, modifier, context.getChangedSlot());
                        UUID uuid = this.getUUID(context.getChangedSlot());
                        if (uuid != null) {
                            if(attribute == AttributeRegistry.ELDRITCH_SPELL_POWER.get()) {
                                attributeModifier = new AttributeModifier(uuid, this.getAttributeModifiername(context.getChangedSlot()), level * 0.6, AttributeModifier.Operation.MULTIPLY_BASE);
                            }
                            else{
                                attributeModifier = new AttributeModifier(uuid, this.getAttributeModifiername(context.getChangedSlot()), -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
                            }
                        }
                        if (attributeModifier != null) {
                            instance.removeModifier(attributeModifier.getId());
                            instance.addTransientModifier(attributeModifier);
                        }
                    }
                }
            }
        }
    }

    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if(context.getEntity().level().isClientSide)return;
        if (tool.getModifier(TTMModifierIds.bibliophilia) != ModifierEntry.EMPTY) {
            UUID uuid = this.getUUID(context.getChangedSlot());
            if (uuid != null) {
                for (SchoolType schoolType : SchoolRegistry.REGISTRY.get()) {
                    Attribute attribute = ((SchoolTypeAccessor)schoolType).getpowerAttribute().orElse(null);
                    if (attribute != null) {
                        AttributeInstance instance = context.getEntity().getAttribute(attribute);
                        if (instance != null) {
                            instance.removeModifier(uuid);
                        }
                    }
                }
            }
        }
    }
    private String getAttributeModifiername(EquipmentSlot equipmentSlot)
    {
        return this.unique + "." + equipmentSlot.getName();
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (tool.getModifier(TTMModifierIds.bibliophilia) != ModifierEntry.EMPTY) {
            int level = tool.getModifierLevel(TTMModifierIds.bibliophilia);
            TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(AttributeRegistry.ELDRITCH_SPELL_POWER.get().getDescriptionId()), 0.6 * level, list);
            list.add(modifier.getModifier().applyStyle(Component.literal(Util.MULTIPLIER_FORMAT.format(0.5) + " ").append(Component.translatable("modifier.touhoutinkermodifier.rlyehtext.anotherspellpower"))));
        }
    }
}
