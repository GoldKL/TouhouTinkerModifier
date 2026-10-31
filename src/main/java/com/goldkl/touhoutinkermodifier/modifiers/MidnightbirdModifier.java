package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.module.PersistentDataBooleanPredicate;
import com.goldkl.touhoutinkermodifier.module.SpellModule;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MidnightbirdModifier extends Modifier implements InventoryTickModifierHook, ModifierRemovalHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.ENDER_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE)
                .toolContext(new PersistentDataBooleanPredicate(TTMModifierIds.midnightbird))
                .uniqueFrom(TTMModifierIds.midnightbird)
                .eachLevel(0.30f));
        hookBuilder.addModule(SpellModule.builder()
                .passMaxlevel(true)
                .passConfig(true)
                .spell(SpellsRegistry.mid_night_bird.get())
                .flat(1));
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK, ModifierHooks.REMOVE);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(tool.getPersistentData().getBoolean(TTMModifierIds.midnightbird) && !world.isClientSide && holder.tickCount % 50 == 0){
            holder.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,60,0));
        }
    }
    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @javax.annotation.Nullable RegistryAccess access) {
        if(tool.getPersistentData().getBoolean(TTMModifierIds.midnightbird))
        {
            return entry.getDisplayName().copy().append(Component.translatable("modifier.touhoutinkermodifier.mid_night_bird.true"));
        }
        return entry.getDisplayName().copy().append(Component.translatable("modifier.touhoutinkermodifier.mid_night_bird.false"));
    }
    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(TTMModifierIds.midnightbird);
        return null;
    }
    public static void updateFlag(IToolStackView tool,boolean flag)
    {
        tool.getPersistentData().putBoolean(TTMModifierIds.midnightbird,flag);
    }
}
