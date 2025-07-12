package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CriticalAttackModifierHook;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.shared.TinkerAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class UnderredmoonModifier extends Modifier {
    //绯色月下：芙兰朵路·斯卡雷特
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(L2DamageTracker.CRIT_DMG.get(), AttributeModifier.Operation.ADDITION).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.25f));
        hookBuilder.addModule(AttributeModule.builder(L2DamageTracker.CRIT_RATE.get(), AttributeModifier.Operation.ADDITION).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.05f));
        hookBuilder.addModule(AttributeModule.builder(ALObjects.Attributes.ARMOR_SHRED.get(), AttributeModifier.Operation.ADDITION).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.05f));

    }
}
