package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.module.SpellModule;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.goldkl.touhoutinkermodifier.tracking.ModifierEvent.NotFightingTime;

public class NaturalwarmogModifier extends Modifier implements InventoryTickModifierHook, ModifierTraitHook {
    //毛玉：天生狂徒
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK, ModifierHooks.MODIFIER_TRAITS);
        hookBuilder.addModule(AttributeModule.builder(Attributes.MAX_HEALTH,
                    AttributeModifier.Operation.ADDITION)
                .uniqueFrom(TTMModifierIds.naturalwarmog)
                .eachLevel(10));
        hookBuilder.addModule(AttributeModule.builder(Attributes.MAX_HEALTH,
                        AttributeModifier.Operation.MULTIPLY_BASE)
                .uniqueFrom(TTMModifierIds.naturalwarmog)
                .eachLevel(0.1f));
        hookBuilder.addModule(AttributeModule.builder(Attributes.MOVEMENT_SPEED,
                        AttributeModifier.Operation.MULTIPLY_BASE)
                .uniqueFrom(TTMModifierIds.naturalwarmog)
                .eachLevel(0.1f));
        hookBuilder.addModule(SpellModule.builder()
                .passMaxlevel(true)
                .passConfig(true)
                .spell(SpellsRegistry.maximumdosage.get())
                .eachLevel(1));
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide || !isCorrectSlot)return;
        holder.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            if(data.getInt(NotFightingTime) > 100){
                int level = modifier.getLevel();
                if(holder.tickCount % 10 == 0){
                    holder.heal(holder.getMaxHealth() * 0.05f * level);
                }
            }
        });
    }
    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        builder.add(TTMModifierIds.naturalgrace, 3 * modifier.getLevel());
    }
}
