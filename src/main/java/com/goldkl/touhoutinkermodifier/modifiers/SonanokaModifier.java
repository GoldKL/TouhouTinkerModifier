package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.EffectApplicableModifierHook;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class SonanokaModifier extends Modifier implements AttributesModifierHook, EffectApplicableModifierHook, InventoryTickModifierHook , TooltipModifierHook {
    final String unique = ModifierIds.sonanoka.getNamespace()+  ".modifier."+ModifierIds.sonanoka.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES,ModifierHooks.TOOLTIP, EtSTLibHooks.EFFECT_APPLICABLE, ModifierHooks.INVENTORY_TICK);
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        int time = 0;
        if(tool.getVolatileData() instanceof ModDataNBT mddata) {
            time = mddata.getInt(ModifierIds.sonanoka);
        }
        if(time == 0) return;
        UUID uuid = this.getUUID(slot);
        if(uuid != null)
        {
            int level = modifierEntry.getLevel();
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.unique + "." + slot.getName(), 0.25 * level, AttributeModifier.Operation.MULTIPLY_BASE);
            biConsumer.accept(AttributeRegistry.ENDER_SPELL_POWER.get(), attributeModifier);
        }
    }

    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }

    @Override
    public Boolean isApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, Boolean notApplicable) {
        if(notApplicable)return true;
        int time = 0;
        if(tool.getVolatileData() instanceof ModDataNBT mddata) {
            time = mddata.getInt(ModifierIds.sonanoka);
            if(time > 0) return false;
            if(instance.getEffect() == MobEffects.BLINDNESS || instance.getEffect() == MobEffects.DARKNESS) {
                mddata.putInt(ModifierIds.sonanoka, instance.getDuration());
                return true;
            }
        }
        return false;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        int time = 0;
        if(tool.getVolatileData() instanceof ModDataNBT mddata) {
            time = mddata.getInt(ModifierIds.sonanoka);
            if(time > 0)
            {
                mddata.putInt(ModifierIds.sonanoka, time - 1);
            }
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int tick = 0;
        if(tool.getVolatileData() instanceof ModDataNBT mddata) {
            tick = mddata.getInt(ModifierIds.sonanoka);
        }
        int time = tick / 20;
        if(tick > 0)
        {
            list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.sonanoka.lefttime",time)));
        }
    }
}
