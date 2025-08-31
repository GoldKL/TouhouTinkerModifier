package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class KoishiseyeModifier extends NoLevelsModifier implements InventoryTickModifierHook , TooltipModifierHook {
    //恋之瞳：古明地恋
    public KoishiseyeModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingEntityUnconsciousCooldownTick);
    }
    private void LivingEntityUnconsciousCooldownTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(ModifierIds.koishiseye);
            if(time > 0)
            {
                data.putInt(ModifierIds.koishiseye, time - 1);
            }
        });
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.TOOLTIP);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(!isCorrectSlot||world.isClientSide)return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(ModifierIds.koishiseye);
            if(time == 0)
            {
                EffectUtil.refreshEffect(entity, new MobEffectInstance(YHEffects.UNCONSCIOUS.get(), 40, 0, true, true), EffectUtil.AddReason.SELF, entity);
            }
        });
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player != null)
        {
            final int[] tick = {0};
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                tick[0] = data.getInt(ModifierIds.koishiseye);
            });
            int time = tick[0] / 20;
            if(tick[0] == 0)
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.koishiseye.can")));
            }
            else
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.koishiseye.cannot",time)));
            }
        }
    }
}
