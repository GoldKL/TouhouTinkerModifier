package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.capability.TheKindofKillDataCapability;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.Util;

import java.text.DecimalFormat;
import java.util.List;

public class HellstrafficaccidentModifier extends Modifier implements MeleeDamagePercentModifierHook, TooltipModifierHook {
    public static final DecimalFormat PERCENT_BOOST_FORMAT_FIX = new DecimalFormat("#.#%");
    static {
        PERCENT_BOOST_FORMAT_FIX.setPositivePrefix("+");
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player == null) return;
        player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(data -> {
            int level = modifier.getLevel();
            float percent = getDamagePercent(level);
            list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.hellstrafficaccident.damage")).append(PERCENT_BOOST_FORMAT_FIX.format(percent) ));
        });
    }
    float getDamagePercent(int level) {
        return 0.25f + 0.125f * level;
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if(attacker instanceof Player player && target != null) {
            player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(data -> {
                int level = modifier.getLevel();
                if(data.containsEntity(target))
                {
                    damagemodifier.addPercent(getDamagePercent(level));
                }
            });
        }
    }
}
