package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AfterPlayerAddedSpellHook;
import com.goldkl.touhoutinkermodifier.mixin.ironspell.SchoolTypeAccessor;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.*;

public class SevenLuminariesWizardModifier extends Modifier implements AfterPlayerAddedSpellHook, TooltipModifierHook, EquipmentChangeModifierHook {
    final String unique = TTMModifierIds.sevenluminarieswizard.getNamespace()+  ".modifier."+ TTMModifierIds.sevenluminarieswizard.getPath();
    final String unique2 = TTMModifierIds.sevenluminarieswizard.getNamespace()+  ".modifier."+ TTMModifierIds.sevenluminarieswizard.getPath() + ".2";
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addModule(AttributeModule.builder(AttributesRegistry.MANA_COST_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE)
                .unique(unique2)
                .eachLevel(0.075f));
        hookBuilder.addHook(this, ModifierHooksRegistry.AFTER_PLAYER_ADDED_SPELL, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.TOOLTIP);
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, double percent) {
        UUID uuid = this.getUUID(slot);
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), percent, AttributeModifier.Operation.MULTIPLY_BASE) : null;
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player == null)return;
        int level = modifier.getLevel();
        player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            ListTag listTag = data.getList(NAME,8);
            listTag.forEach(tag -> {
                ResourceLocation resourceLocation = ResourceLocation.parse(tag.getAsString());
                SchoolType schoolType = SchoolRegistry.getSchool(resourceLocation);
                if(schoolType != null){
                    Attribute attribute = ((SchoolTypeAccessor)schoolType).getpowerAttribute().get();
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(attribute.getDescriptionId()),getPercent(level,listTag.size()), list);
                }
            });
        });
    }
    private double getPercent(int level,int kind)
    {
        return level * kind * 0.025;
    }
    @Override
    public void afterPlayerAddedSpell(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager) {
        LivingEntity entity = context.getEntity();
        if (!entity.level().isClientSide && entity instanceof Player player) {
            updateSpellKind(player,manager);
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                ListTag listTag = data.getList(NAME,8);
                listTag.forEach(tag -> {
                    ResourceLocation resourceLocation = ResourceLocation.parse(tag.getAsString());
                    SchoolType schoolType = SchoolRegistry.getSchool(resourceLocation);
                    if(schoolType != null){
                        Attribute attribute = ((SchoolTypeAccessor)schoolType).getpowerAttribute().get();
                        AttributeInstance instance = player.getAttribute(attribute);
                        if(instance != null){
                            AttributeModifier attributeModifier = createModifier(tool,modifier,slotType,getPercent(modifier.getLevel(),listTag.size()));
                            if(attributeModifier != null){
                                instance.removeModifier(attributeModifier);
                                instance.addTransientModifier(attributeModifier);
                            }
                        }
                    }
                });
            });
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        UUID uuid = this.getUUID(context.getChangedSlot());
        LivingEntity entity = context.getEntity();
        if(entity instanceof Player player)
        {
            SpellSelectionManager manager = new SpellSelectionManager(player);
            updateSpellKind(player,manager);
            if(uuid != null)
            {
                for (SchoolType schoolType : SchoolRegistry.REGISTRY.get()){
                    Attribute attribute = ((SchoolTypeAccessor)schoolType).getpowerAttribute().get();
                    if(attribute != null)
                    {
                        AttributeInstance instance = context.getEntity().getAttribute(attribute);
                        if (instance != null) {
                            instance.removeModifier(uuid);
                        }
                    }
                }
            }
        }
    }
    static ResourceLocation NAME = TouhouTinkerModifier.getResource("sevenluminarieswizard");
    static void updateSpellKind(LivingEntity entity,SpellSelectionManager manager){
        if(entity instanceof Player player && !player.level().isClientSide){
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                data.remove(NAME);
                Set<String> set = new HashSet<>();
                manager.getAllSpells().forEach((selectionOption -> {
                    String tag = selectionOption.spellData.getSpell().getSchoolType().getId().toString();
                    set.add(tag);
                }));
                ListTag list = new ListTag();
                set.forEach( name ->{
                    list.add(StringTag.valueOf(name));
                });
                data.put(NAME,list);
                TinkerNetwork.getInstance().sendToTrackingAndSelf(new SyncPersistentDataPacket(data.getCopy()), entity);
            });
        }
    }
}
