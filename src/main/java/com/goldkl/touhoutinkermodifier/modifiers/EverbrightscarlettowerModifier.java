package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class EverbrightscarlettowerModifier extends NoLevelsModifier implements AttributesModifierHook {
    //不夜红楼：小恶魔
    final String unique = ModifierIds.everbrightscarlettower.getNamespace()+  ".modifier."+ModifierIds.everbrightscarlettower.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(AttributeRegistry.COOLDOWN_REDUCTION.get(), PerkAttributes.MANA_REGEN_BONUS.get());
    private static final List<Float> attributes_amount = List.of(0.05f,5f);
    private static final List<AttributeModifier.Operation> attribute_operation = List.of(AttributeModifier.Operation.MULTIPLY_BASE,AttributeModifier.Operation.ADDITION);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.ATTRIBUTES);
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (ModifierCondition.ANY_TOOL.matches(tool, modifier)) {
            int level = TTMEntityUtils.gettotallevelwithtag(tool,TagsRegistry.ModifiersTag.ScarletDevilMansion);
            for(int i = 0; i < 2; i++) {
                AttributeModifier attributeModifier = this.createModifier(tool, modifier, slot, level,i);
                if (attributeModifier != null) {
                    consumer.accept(attributes.get(i), attributeModifier);
                }
            }
        }
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot,int level,int index) {
        UUID uuid = this.getUUID(slot);
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), level * attributes_amount.get(index), attribute_operation.get(index)) : null;
    }
}
