package com.goldkl.touhoutinkermodifier.module;

import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.EnumLoadable;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.json.math.ModifierFormula;
import slimeknights.tconstruct.library.json.variable.VariableFormula;
import slimeknights.tconstruct.library.json.variable.VariableFormulaLoadable;
import slimeknights.tconstruct.library.json.variable.tool.ToolFormula;
import slimeknights.tconstruct.library.json.variable.tool.ToolVariable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeUniqueField;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import com.ssakura49.sakuratinker.library.hooks.curio.armor.CurioEquipmentChangeModifierHook;
import com.ssakura49.sakuratinker.library.hooks.curio.behavior.CurioAttributeModifierHook;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record CurioAttributeModule(String unique, Attribute attribute, AttributeModifier.Operation operation, ToolFormula formula, UUID[] slotUUIDs, boolean blacklist, Set<String> slotslist, TooltipStyle tooltipStyle, ModifierCondition<IToolStackView> condition)
        implements ModifierModule,
        CurioAttributeModifierHook, CurioEquipmentChangeModifierHook,
        AttributesModifierHook, EquipmentChangeModifierHook,
        TooltipModifierHook, ModifierCondition.ConditionalModule<IToolStackView> {
    private static final String[] VARIABLES = { "level" };
    private static final RecordLoadable<ToolFormula> VARIABLE_LOADER = new VariableFormulaLoadable<>(ToolVariable.LOADER, VARIABLES, ModifierFormula.FallbackFormula.IDENTITY, (formula, variables, percent) -> new ToolFormula(formula, variables, VariableFormula.EMPTY_STRINGS));
    private static final List<ModuleHook<?>> ATTRIBUTE_HOOKS = HookProvider.<CurioAttributeModule>defaultHooks(STHooks.CURIO_ATTRIBUTE,ModifierHooks.ATTRIBUTES);
    private static final List<ModuleHook<?>> TOOLTIP_HOOKS = HookProvider.<CurioAttributeModule>defaultHooks(STHooks.CURIO_EQUIPMENT_CHANGE,ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.TOOLTIP);
    private static final List<ModuleHook<?>> NO_TOOLTIP_HOOKS = HookProvider.<CurioAttributeModule>defaultHooks(STHooks.CURIO_EQUIPMENT_CHANGE,ModifierHooks.EQUIPMENT_CHANGE);

    public static final RecordLoadable<CurioAttributeModule> LOADER = RecordLoadable.create(
            new AttributeUniqueField<>(CurioAttributeModule::unique),
            Loadables.ATTRIBUTE.requiredField("attribute", CurioAttributeModule::attribute),
            TinkerLoadables.OPERATION.requiredField("operation", CurioAttributeModule::operation),
            VARIABLE_LOADER.directField(CurioAttributeModule::formula),
            TinkerLoadables.EQUIPMENT_SLOT.set(0).nullableField("slots", m -> AttributeModule.uuidsToSlots(m.slotUUIDs)),
            BooleanLoadable.INSTANCE.defaultField("black_list", false, CurioAttributeModule::blacklist),
            StringLoadable.DEFAULT.set(0).nullableField("slot_list", CurioAttributeModule::slotslist),
            TooltipStyle.LOADABLE.defaultField("tooltip_style", TooltipStyle.ATTRIBUTE, CurioAttributeModule::tooltipStyle),
            ModifierCondition.TOOL_FIELD,
            (unique,attribute,operation,formula,slots,blacklist,slotslist,tooltipStyle,condition)
                    ->new CurioAttributeModule(unique,attribute,operation,formula,AttributeModule.slotsToUUIDs(unique,slots),blacklist,slotslist,tooltipStyle,condition));
            //CurioAttributeModule::new);

    @Nullable
    public UUID getUUID(SlotContext slot) {
        if(this.blacklist == this.slotslist.contains(slot.identifier())) return null;
        return UUID.nameUUIDFromBytes((unique + "." + slot.identifier() + "." + slot.index()).getBytes());
    }
    @Nullable
    private UUID getUUID(EquipmentSlot slot) {
        return slotUUIDs[slot.getFilterFlag()];
    }
    /** Creates a new builder instance */
    public static Builder builder(Attribute attribute, AttributeModifier.Operation operation) {
        return new Builder(attribute, operation);
    }

    public static Builder builder(Supplier<Attribute> attribute, AttributeModifier.Operation operation) {
        return new Builder(attribute.get(), operation);
    }
    public static void addTooltip(Modifier modifier, Attribute attribute, AttributeModifier.Operation operation, TooltipStyle tooltipStyle, float amount, @Nullable UUID uuid, @Nullable Player player, List<Component> tooltip) {
        switch (tooltipStyle) {
            case ATTRIBUTE -> TooltipUtil.addAttribute(attribute, operation, amount, uuid, player, tooltip);
            case BOOST -> TooltipModifierHook.addFlatBoost(modifier, Component.translatable(attribute.getDescriptionId()), amount, tooltip);
            case PERCENT -> TooltipModifierHook.addPercentBoost(modifier, Component.translatable(attribute.getDescriptionId()), amount, tooltip);
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (condition.matches(tool, modifier)) {
            float value = formula.apply(tool, modifier);
            if (value != 0) {
                addTooltip(modifier.getModifier(), attribute, operation, tooltipStyle, value, null, player, tooltip);
            }
        }
    }

    @NotNull
    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }
    @Nullable
    private AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, SlotContext slot) {
        UUID uuid = getUUID(slot);
        if (uuid != null) {
            return new AttributeModifier(uuid, unique + "." + slot.identifier() + "." + slot.index(), formula.apply(tool, modifier), operation);
        }
        return null;
    }
    @Nullable
    private AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot) {
        UUID uuid = getUUID(slot);
        if (uuid != null) {
            return new AttributeModifier(uuid, unique + "." + slot.getName(), formula.apply(tool, modifier), operation);
        }
        return null;
    }
    @Override
    public void onCurioEquip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack prevStack, ItemStack stack) {
        if (condition.matches(curio, entry)) {
            AttributeInstance instance = entity.getAttribute(attribute);
            if (instance != null) {
                AttributeModifier attributeModifier = createModifier(curio, entry, context);
                if (attributeModifier != null) {
                    instance.removeModifier(attributeModifier.getId());
                    instance.addTransientModifier(attributeModifier);
                }
            }
        }
    }

    @Override
    public void onCurioUnequip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack newStack, ItemStack stack) {
        if (condition.matches(curio, entry)) {
            UUID uuid = getUUID(context);
            if (uuid != null) {
                AttributeInstance instance = entity.getAttribute(attribute);
                if (instance != null) {
                    instance.removeModifier(uuid);
                }
            }
        }
    }

    @Override
    public void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (condition.matches(curio, entry)) {
            AttributeModifier attributeModifier = createModifier(curio, entry, context);
            if (attributeModifier != null) {
                consumer.accept(attribute, attributeModifier);
            }
        }
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        if (tooltipStyle == TooltipStyle.ATTRIBUTE) {
            return ATTRIBUTE_HOOKS;
        }
        if (tooltipStyle == TooltipStyle.NONE) {
            return NO_TOOLTIP_HOOKS;
        }
        return TOOLTIP_HOOKS;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (condition.matches(tool, modifier)) {
            AttributeModifier attributeModifier = createModifier(tool, modifier, slot);
            if (attributeModifier != null) {
                consumer.accept(attribute, attributeModifier);
            }
        }
    }
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (condition.matches(tool, modifier)) {
            AttributeInstance instance = context.getEntity().getAttribute(attribute);
            if (instance != null) {
                AttributeModifier attributeModifier = createModifier(tool, modifier, context.getChangedSlot());
                if (attributeModifier != null) {
                    // for safety, remove it already there
                    instance.removeModifier(attributeModifier.getId());
                    instance.addTransientModifier(attributeModifier);
                }
            }
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (condition.matches(tool, modifier)) {
            UUID uuid = getUUID(context.getChangedSlot());
            if (uuid != null) {
                AttributeInstance instance = context.getEntity().getAttribute(attribute);
                if (instance != null) {
                    instance.removeModifier(uuid);
                }
            }
        }
    }

    public enum TooltipStyle {
        ATTRIBUTE, NONE, BOOST, PERCENT;
        public static final EnumLoadable<TooltipStyle> LOADABLE = new EnumLoadable<>(TooltipStyle.class);
    }
    public static class Builder extends VariableFormula.Builder<Builder, CurioAttributeModule, ToolVariable> {
        protected final Attribute attribute;
        protected final AttributeModifier.Operation operation;
        protected String unique = "";
        protected boolean blacklist = false;
        private final Set<String> slotslist= new HashSet<>();
        private TooltipStyle tooltipStyle;
        private EquipmentSlot[] slots = EquipmentSlot.values();

        protected Builder(Attribute attribute, AttributeModifier.Operation operation) {
            super(VARIABLES);
            this.tooltipStyle = TooltipStyle.ATTRIBUTE;
            this.attribute = attribute;
            this.operation = operation;
        }

        public Builder slots(String... slots) {
            slotslist.addAll(Arrays.asList(slots));
            return this;
        }
        public Builder slots(EquipmentSlot... slots) {
            this.slots = slots;
            return this;
        }

        public Builder uniqueFrom(ResourceLocation id) {
            String var10001 = id.getNamespace();
            return this.unique(var10001 + ".modifier." + id.getPath());
        }

        protected CurioAttributeModule build(ModifierFormula formula) {
            return new CurioAttributeModule(this.unique, this.attribute, this.operation, new ToolFormula(formula, this.variables), AttributeModule.slotsToUUIDs(unique, List.of(slots)), this.blacklist, this.slotslist, this.tooltipStyle, this.condition);
        }

        public Builder unique(String unique) {
            this.unique = unique;
            return this;
        }

        public Builder tooltipStyle(TooltipStyle tooltipStyle) {
            this.tooltipStyle = tooltipStyle;
            return this;
        }

        public Builder blacklist(boolean blacklist) {
            this.blacklist = blacklist;
            return this;
        }
    }
}
