package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.data.ModifierMaxLevel;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.MaxArmorLevelModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class GreeneyesModifier extends Modifier implements MaxArmorLevelModule , EquipmentChangeModifierHook, InventoryTickModifierHook {
    private static final UUID uuid = UUID.nameUUIDFromBytes((ModifierIds.greeneyes.toString()).getBytes());
    private static final List<Attribute> attributes = List.of(Attributes.MAX_HEALTH,Attributes.ARMOR);
    private static final List<Float> attributes_amount = List.of(4.0f,2.0f);
    private static final TinkerDataCapability.ComputableDataKey<ModifierMaxLevel> maxLevel = MaxArmorLevelModule.createKey(ModifierIds.greeneyes);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public @NotNull TinkerDataCapability.ComputableDataKey<ModifierMaxLevel> maxLevel() {
        return maxLevel;
    }

    @Override
    public boolean allowBroken() {
        return false;
    }

    @Override
    public @Nullable TagKey<Item> heldTag() {
        return null;
    }

    @Override
    public void updateValue(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context, TinkerDataCapability.Holder data,float newLevel,float oldLevel) {
        updataeValue(context.getEntity(),newLevel);
    }

    void updataeValue(LivingEntity entity, float level)
    {
        int[] count = {0 , 0};
        for(MobEffectInstance effect : entity.getActiveEffects())
        {
            if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
            {
                count[0]++;
            }
            else if(effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL)
            {
                count[1]++;
            }
        }
        for(int i = 0; i < 2; ++i)
        {
            AttributeInstance instance = entity.getAttribute(attributes.get(i));
            if (instance != null) {
                instance.removeModifier(uuid);
                float attributeValue = level*attributes_amount.get(i)*count[i];
                if (attributeValue != 0) {
                    instance.addTransientModifier(new AttributeModifier(uuid, ModifierIds.greeneyes.toString(), attributeValue, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }
    @Override
    public @NotNull ModifierCondition<IToolStackView> condition() {
        return ModifierCondition.ANY_TOOL;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return NO_TOOLTIP_HOOKS;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(!world.isClientSide) {
            if (isCorrectSlot) {
                livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                    ModifierMaxLevel maxLevel = (ModifierMaxLevel)data.get(this.maxLevel());
                    if(maxLevel!=null)
                    {
                        float level = maxLevel.getMax();
                        updataeValue(livingEntity,level);
                    }
                });
            }
        }
    }
}
