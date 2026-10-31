package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class WhereaboutsoftheHeartModifier extends Modifier implements ModifyDamageModifierHook {
    //心之所在：纯狐
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.WhereaboutsoftheHeart);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
    }
    @Override
    public int getPriority() {
        return 1000;
    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(source.is(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER))return amount;
        int level = SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
        if(level > 0)
        {
            LivingEntity entity = context.getEntity();
            double max_damage_per = 0.5 + 1.0 / (2.0 + level);
            double min_damage_per = 0.5 - 1.0 / (2.0 + level);
            double max_damage = max_damage_per * entity.getMaxHealth();
            double min_damage = min_damage_per * entity.getMaxHealth();
            if(amount > max_damage){
                return (float) max_damage;
            }else if(amount <= min_damage){
                return 0;
            }
            return (float) ((amount - min_damage)/(max_damage - min_damage) * max_damage);
        }
        return amount;
    }
}
