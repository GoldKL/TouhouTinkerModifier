package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.module.SpellModule;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.json.math.ModifierFormula;
import slimeknights.tconstruct.library.json.predicate.tool.ToolContextPredicate;
import slimeknights.tconstruct.library.json.predicate.tool.ToolStackPredicate;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.VolatileFlagModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;

public class OozoramajutsuModifier extends Modifier implements ToolStatsModifierHook, InventoryTickModifierHook, ModifierRemovalHook {
    public static ResourceLocation NEED_REBUILD = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID,"oozoramajutsumodifie_needrebuild");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.INVENTORY_TICK, ModifierHooks.REMOVE);
        hookBuilder.addModule(SpellModule
                .builder()
                .tool(ToolStackPredicate.simple(tool -> (SecretsealingclubModifier.SecretSealingClubcanUse(tool) || tool.hasTag(TagsRegistry.ItemsTag.TECHNOLOGY))))
                .passConfig(true)
                .passMaxlevel(true)
                .spell(SpellsRegistry.tinkercharge.get())
                .amount(1,0));
    }
    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(TTMModifierIds.oozoramajutsu);
        tool.getPersistentData().remove(NEED_REBUILD);
        return null;
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        if(context.getPersistentData().getBoolean(TTMModifierIds.oozoramajutsu))
        {
            float amount = level + 1;
            if (amount > 0) {
                if (context.hasTag(TinkerTags.Items.MELEE)) {
                    ToolStats.ATTACK_DAMAGE.multiply(builder, amount);
                }
                if (context.hasTag(TinkerTags.Items.HARVEST)) {
                    ToolStats.MINING_SPEED.multiply(builder, amount);
                }
                if (context.hasTag(TinkerTags.Items.ARMOR)) {
                    ToolStats.ARMOR.multiply(builder, amount);
                }
                if (context.hasTag(TinkerTags.Items.RANGED)) {
                    ToolStats.VELOCITY.multiply(builder, amount);
                }
            }
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(tool.getPersistentData().getBoolean(TTMModifierIds.oozoramajutsu)){
            ToolEnergyCapability.setEnergy(tool,ToolEnergyCapability.getEnergy(tool) - Math.min(Integer.MAX_VALUE,(int)(250 * Math.pow(2 , modifier.getLevel()))));
            if(ToolEnergyCapability.getEnergy(tool) <= 0){
                updateFlag(tool, false);
            }
        }
        if(tool.getPersistentData().getBoolean(NEED_REBUILD)){
            ToolStack.from(stack).rebuildStats();
            tool.getPersistentData().remove(NEED_REBUILD);
        }
    }
    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        if(tool.getPersistentData().getBoolean(TTMModifierIds.oozoramajutsu))
        {
            return entry.getDisplayName().copy().append(Component.translatable("modifier.touhoutinkermodifier.oozoramajutsu.true"));
        }
        return entry.getDisplayName().copy().append(Component.translatable("modifier.touhoutinkermodifier.oozoramajutsu.false"));
    }
    public static void updateFlag(IToolStackView tool,boolean flag)
    {
        tool.getPersistentData().putBoolean(TTMModifierIds.oozoramajutsu,flag);
        tool.getPersistentData().putBoolean(NEED_REBUILD,true);
    }
}
