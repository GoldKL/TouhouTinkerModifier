package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.UUID;


public class DevourdarknessModifier extends Modifier implements InventoryTickModifierHook, EquipmentChangeModifierHook,ProcessLootModifierHook, TooltipModifierHook {
    //吞噬黑暗：EX露米娅
    public DevourdarknessModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingEntityEatFlesh);
        MinecraftForge.EVENT_BUS.addListener(this::LivingEntityFleshTick);
    }
    private void LivingEntityEatFlesh(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        ItemStack usedItem = event.getItem();
        if (!usedItem.isEdible()) return;
        if (!usedItem.is(YHTagGen.FLESH_FOOD))return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            data.putInt(TTMModifierIds.devourdarkness, 12000);
        });
    }
    private void LivingEntityFleshTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(TTMModifierIds.devourdarkness);
            if(time > 0)
            {
                data.putInt(TTMModifierIds.devourdarkness, time - 1);
            }
        });
    }
    final String unique = TTMModifierIds.devourdarkness.getNamespace()+  ".modifier."+ TTMModifierIds.devourdarkness.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT,ModifierHooks.TOOLTIP,ModifierHooks.INVENTORY_TICK,ModifierHooks.EQUIPMENT_CHANGE);
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide()||!isCorrectSlot)return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
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
                boolean flag = false;
                int time = data.getInt(TTMModifierIds.devourdarkness);
                if(time > 0)flag = true;
                AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
                if (instance != null) {
                    UUID uuid = this.getUUID(slot);
                    if (uuid != null) {
                        instance.removeModifier(uuid);
                        if (!flag) {
                            instance.addTransientModifier(new AttributeModifier(uuid, this.unique + "." + slot.getName(),-0.3, AttributeModifier.Operation.MULTIPLY_TOTAL));
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        UUID uuid = this.getUUID(context.getChangedSlot());
        if (uuid != null) {
            AttributeInstance instance = context.getEntity().getAttribute(Attributes.MAX_HEALTH);
            if (instance != null) {
                instance.removeModifier(uuid);
            }
        }
    }
    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
            return;
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity attacker = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity instanceof Mob) {
            int level = 0;
            if (attacker instanceof LivingEntity lvingentitiy) {
                for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
                {
                    if(!(lvingentitiy.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
                    ToolStack nwtool = ToolStack.from(lvingentitiy.getItemBySlot(equipmentSlot));
                    level += nwtool.getModifier(TTMModifierIds.devourdarkness).intEffectiveLevel();
                }
            }
            else
            {
                level = modifier.intEffectiveLevel();
            }
            if(level == 0) level = 1;
            int looting = context.getLootingModifier();
            int num = 12 / level;
            if(num < 1) num = 1;
            int total = 0;
            if (RANDOM.nextInt(num) <= looting) {
                total ++;
            }
            if (RANDOM.nextInt(2 * num) <= looting) {
                total ++;
            }
            if(total > 0)
            {
                generatedLoot.add(new ItemStack(YHFood.FLESH.item.get(),total));
            }
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player != null)
        {
            final int[] tick = {0};
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                tick[0] = data.getInt(TTMModifierIds.devourdarkness);
            });
            int time = tick[0] / 20;
            if(tick[0] > 0)
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.devourdarkness.nottoeat",time)));
            }
            else
            {
                TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(Attributes.MAX_HEALTH.getDescriptionId()),-0.3, list);
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.devourdarkness.needtoeat")));
            }
        }
    }
}
