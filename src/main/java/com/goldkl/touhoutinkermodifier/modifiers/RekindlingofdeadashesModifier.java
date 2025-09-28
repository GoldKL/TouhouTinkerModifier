package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RekindlingofdeadashesModifier extends Modifier implements InventoryTickModifierHook, EquipmentChangeModifierHook, TooltipModifierHook, MeleeHitModifierHook {
    //死灰复燃：火焰猫燐
    final String unique = TTMModifierIds.rekindlingofdeadashes.getNamespace()+  ".modifier."+ TTMModifierIds.rekindlingofdeadashes.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(Attributes.ATTACK_DAMAGE,Attributes.ARMOR);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.TOOLTIP, ModifierHooks.MELEE_HIT);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if(attacker.getHealth() * 2 <= attacker.getMaxHealth() && target != null) {
            int maxlevel = modifier.getLevel() + 2;
            int nowlevel = -1;
            MobEffectInstance instance = target.getEffect(MobeffectRegistry.DEATHFIRE.get());
            if(instance != null) {
                nowlevel = instance.getAmplifier();
            }
            nowlevel = Math.min(nowlevel + 1, maxlevel);
            target.addEffect(new MobEffectInstance(MobeffectRegistry.DEATHFIRE.get(), 600, nowlevel));
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        if(TTMEntityUtils.validArmorTool(tool,isCorrectSlot,entity,itemStack))
        {
            EquipmentSlot slot = null;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(entity.getItemBySlot(equipmentSlot) == itemStack)
                {
                    slot = equipmentSlot;
                    break;
                }
            }
            if(slot != null)
            {
                int level = modifier.getLevel();
                for(int i = 0; i < 2; i++) {
                    AttributeInstance instance = entity.getAttribute(attributes.get(i));
                    if(instance != null)
                    {
                        AttributeModifier attributeModifier = this.createModifier(tool, modifier, slot, level,getPercent(entity),i);
                        if (attributeModifier != null) {
                            instance.removeModifier(attributeModifier);
                            instance.addTransientModifier(attributeModifier);
                        }
                    }
                }
            }
        }
    }
    private double getPercent(LivingEntity entity)
    {
        double healthpercent = entity.getHealth() / entity.getMaxHealth();
        if(healthpercent >= 0.6)
            return 0;
        else if(healthpercent <= 0.3)
            return 1;
        else return 2 - healthpercent / 0.3;
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, int level, double percent, int index) {
        UUID uuid = this.getUUID(slot);
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), level * percent / 10.0, AttributeModifier.Operation.MULTIPLY_BASE) : null;
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player == null)return;
        int level = modifier.getLevel();
        for(int i = 0; i < 2; i++) {
            TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(attributes.get(i).getDescriptionId()), level * getPercent(player) / 10.0, list);
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        UUID uuid = this.getUUID(context.getChangedSlot());
        if(uuid != null)
        {
            for(int i = 0; i < 2; i++) {
                AttributeInstance instance = context.getEntity().getAttribute(attributes.get(i));
                if(instance != null)
                {
                    instance.removeModifier(uuid);
                }
            }
        }
    }
}
