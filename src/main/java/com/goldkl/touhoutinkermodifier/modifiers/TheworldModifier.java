package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierRequirementsModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TheworldModifier extends Modifier implements GeneralInteractionModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
        hookBuilder.addModule(ModifierRequirementsModule.builder().requireModifier(ModifierIds.jacktheripper,1).requireModifier(ModifierIds.vanhelsingprogeny,1).modifierKey(ModifierIds.theworld).build());
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if (source == InteractionSource.RIGHT_CLICK && !tool.isBroken() && player.getFoodData().getFoodLevel() > 0 && modifier.intEffectiveLevel() > 0) {
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 16;
    }
    @Override
    public void onFinishUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity) {
        int level = modifier.intEffectiveLevel();
        if(level > 0) {
            if (!tool.isBroken() && entity instanceof Player player) {
                if(player.hasEffect(MobeffectRegistry.TIMESTOP.get()))
                {
                    player.removeEffect(MobeffectRegistry.TIMESTOP.get());
                }
                else
                {
                    player.addEffect(new MobEffectInstance(MobeffectRegistry.TIMESTOP.get(),
                            20 + 20*level,
                            level/2,
                            false,
                            true));
                }
            }
        }
    }
}
