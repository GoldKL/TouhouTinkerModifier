package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class MidnightbirdModifier extends Modifier implements AttributesModifierHook {
    public MidnightbirdModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::SpellPreCast);
    }
    void SpellPreCast(SpellPreCastEvent event)
    {
        if(event.isCanceled())return;
        Player player = event.getEntity();
        boolean flag = false;
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(!(player.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(player.getItemBySlot(equipmentSlot));
            if(tool.getModifier(TTMModifierIds.midnightbird)!=ModifierEntry.EMPTY)
            {
                flag = true;
                break;
            }
        }
        if(flag)
        {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,60, 0, false, true));
        }
    }
    final String unique = TTMModifierIds.midnightbird.getNamespace()+  ".modifier."+ TTMModifierIds.midnightbird.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        UUID uuid = this.getUUID(slot);
        if(uuid != null)
        {
            int level = modifierEntry.getLevel();
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.unique + "." + slot.getName(), 0.15 * level, AttributeModifier.Operation.MULTIPLY_BASE);
            biConsumer.accept(AttributeRegistry.ENDER_SPELL_POWER.get(), attributeModifier);
        }
    }

    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
}
