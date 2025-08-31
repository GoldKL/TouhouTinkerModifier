package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ThepowerofmountainModifier extends Modifier implements InventoryTickModifierHook {
    //山之四天王[力]：星熊勇仪
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide)return;
        boolean flag = TTMEntityUtils.validArmorTool(tool,isCorrectSlot,holder,stack);
        if(flag && holder.hasEffect(YHEffects.DRUNK.get()))
        {
            MobEffectInstance instance = holder.getEffect(YHEffects.DRUNK.get());
            if(instance != null)
            {
                int duration = instance.getDuration();
                MobEffectInstance instance_tem = holder.getEffect(MobeffectRegistry.FINEDRUNK.get());
                int level = Math.min(instance_tem != null?instance_tem.getAmplifier() + 1: 0, 4);
                holder.removeEffect(YHEffects.DRUNK.get());
                holder.addEffect(new MobEffectInstance(MobeffectRegistry.FINEDRUNK.get(),duration,level));
            }
        }
    }
}
