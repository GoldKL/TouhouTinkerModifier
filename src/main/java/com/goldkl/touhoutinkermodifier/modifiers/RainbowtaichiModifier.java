package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


import java.util.function.BiFunction;

public class RainbowtaichiModifier extends Modifier implements ProtectionModifierHook, EquipmentChangeModifierHook, InventoryTickModifierHook, AttackerWithEquipmentModifyDamageModifierHook, ProjectileLaunchModifierHook {//MeleeDamageModifierHook,
    //虹色太极：红美铃
    public static TinkerDataCapability.TinkerDataKey<Integer> tinkerDataKey = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.rainbowtaichi);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.PROTECTION, ModifierHooks.INVENTORY_TICK, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);//ModifierHooks.MELEE_DAMAGE
    }

    @Override
    public void onProjectileLaunch(IToolStackView iToolStackView, ModifierEntry modifierEntry, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, ModDataNBT modDataNBT, boolean b) {
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        int num = shooter.getCapability(TinkerDataCapability.CAPABILITY).map(data->data.get(tinkerDataKey,0)).orElse(0);
        if(arrow != null)
        {
            int level = TTMEntityUtils.getModifiertotalLevel(shooter, modifier.getId());
            if(level > 0)
            {
                arrow.setBaseDamage(arrow.getBaseDamage() * (1 + num / 200.0 * 0.1 * level));
            }
            shooter.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(holder -> holder.put(tinkerDataKey,0));
        }
    }

    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        int num = context.getTinkerData().map(data->data.get(tinkerDataKey,0)).orElse(0);
        if(num != 0)
        {
            int level = TTMEntityUtils.getModifiertotalLevel(context.getEntity(), modifier.getId());
            if(level > 0)
            {
                damageModifier.addPercent((float) num /200 * 0.3f * level);
            }
            context.getTinkerData().ifPresent(holder -> holder.put(tinkerDataKey,0));
        }
    }

    @Override
    public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue) {
        modifierValue += context.getTinkerData().map(data ->{
            int num = data.get(tinkerDataKey,0);
            int temp = modifier.getLevel() * 10;
            return Math.max(10.0f*temp/(num + temp),0.25f);
        }).orElse(0.0f);
        return modifierValue;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide())return;
        if(TTMEntityUtils.validArmorTool(tool,isCorrectSlot,holder,stack))
        {
            EquipmentSlot slot = null;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(holder.getItemBySlot(equipmentSlot) == stack)
                {
                    slot = equipmentSlot;
                    break;
                }
            }
            if(slot != null)
            {
                holder.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data ->{
                    int num = data.get(tinkerDataKey,0);
                    int level = TTMEntityUtils.getModifiertotalLevel(holder, modifier.getId());
                    if(num < 400 * level) {
                        num++;
                        data.put(tinkerDataKey,num);
                    }
                });
            }
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if(!TTMEntityUtils.hasModifier(context.getEntity(), TTMModifierIds.rainbowtaichi)){
            context.getTinkerData().ifPresent(data-> data.remove(tinkerDataKey));
        }
    }
}
