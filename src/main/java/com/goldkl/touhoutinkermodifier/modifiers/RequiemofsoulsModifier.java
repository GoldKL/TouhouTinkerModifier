package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.capability.TheKindofKillDataCapability;
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

import java.util.List;

public class RequiemofsoulsModifier extends Modifier implements MeleeDamagePercentModifierHook, TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);
    }
    private static final int COMMON_MAX = 100;
    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player == null) return;
        player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(data -> {
            int level = modifier.getLevel();
            int num1 = data.getCount(false);
            int num1_max = level * COMMON_MAX;
            int num2 = data.getCount(true);
            String num1_toshow = (num1 <= num1_max? String.valueOf(num1):"\033[31m"+num1_max+"\033[0m")+'/'+num1_max;
            list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.requiemofsouls.killnum", num1_toshow, num2)));
            list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.requiemofsouls.damageadd",getDamageAdd(num1, num2, level))));
        });
    }
    float getDamageAdd(int num1, int num2, int level) {
        return (Math.min(num1,level * COMMON_MAX) * 0.25f  + num2 * 1f)*level;
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier) {
        LivingEntity attacker = context.getAttacker();
        if(attacker instanceof Player player) {
            player.getCapability(TheKindofKillDataCapability.CAPABILITY).ifPresent(data -> {
                int level = modifier.getLevel();
                int num1 = data.getCount(false);
                int num2 = data.getCount(true);
                damagemodifier.addAdd(getDamageAdd(num1, num2, level));
            });
        }
    }
}
