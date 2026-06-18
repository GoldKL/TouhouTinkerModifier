package com.goldkl.touhoutinkermodifier.mixin.tconstruct;

import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.ModifierLootModifier;

@Mixin(ModifierLootModifier.class)
public class ModifierLootModifierMixin {
    @Inject(method = "doApply",at = @At("RETURN"),remap = false)
    void doApplyMixin(ObjectArrayList<ItemStack> generatedLoot, LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir){
        if(context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof LivingEntity lv){
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(lv.getItemBySlot(slot));
                if(tool != null && !tool.isBroken() && ModifierUtil.validArmorSlot(tool, slot)){
                    for (ModifierEntry entry:tool.getModifierList()){
                        entry.getHook(ModifierHooksRegistry.PROCESS_ARMOR_LOOT_MODIFIER).processArmorLoot(tool,entry,lv,generatedLoot, context,slot);
                    }
                }
            }
        }
        else if(context.getParamOrNull(LootContextParams.BLOCK_STATE) != null && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity lv){
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(lv.getItemBySlot(slot));
                if(tool != null && !tool.isBroken() && ModifierUtil.validArmorSlot(tool, slot)){
                    for (ModifierEntry entry:tool.getModifierList()){
                        entry.getHook(ModifierHooksRegistry.PROCESS_ARMOR_LOOT_MODIFIER).processArmorLoot(tool,entry,lv,generatedLoot, context,slot);
                    }
                }
            }
        }
    }
}
