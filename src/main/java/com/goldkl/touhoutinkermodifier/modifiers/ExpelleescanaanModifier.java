package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

public class ExpelleescanaanModifier extends Modifier implements OnAttackedModifierHook, ToolStatsModifierHook {
    //应许之地：犬走椛
    static int PARRYTICK = 10;
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED, ModifierHooks.TOOL_STATS);
    }
    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(context.getEntity().isBlocking()) {
            if (isDirectDamage && source.getEntity() instanceof LivingEntity attacker) {
                if(slotType.getType() == EquipmentSlot.Type.HAND)
                {
                    if(context.getEntity().getTicksUsingItem() <= PARRYTICK)
                    {
                        attacker.addEffect(new MobEffectInstance(MobeffectRegistry.FRAGILE.get(),60,2,false,true));
                        attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10, false, true));
                    }
                }
            }
        }
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if(context.getModifier(TinkerModifiers.blocking.get()) != ModifierEntry.EMPTY)
            if (context.hasTag(TinkerTags.Items.SHIELDS))
            {
                ToolStats.BLOCK_AMOUNT.multiply(builder,2.0);
            }
            else
            {
                ToolStats.BLOCK_AMOUNT.add(builder,50);
            }
    }
}
