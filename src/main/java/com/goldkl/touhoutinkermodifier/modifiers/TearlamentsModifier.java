package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.NightVisionHook;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.mojang.blaze3d.shaders.FogShape;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TearlamentsModifier extends Modifier implements InventoryTickModifierHook, TooltipModifierHook, EquipmentChangeModifierHook, NightVisionHook {
    //珠泪哀歌：若鹭姬
    final String unique = TTMModifierIds.tearlaments.getNamespace()+  ".modifier."+ TTMModifierIds.tearlaments.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(
            Attributes.ATTACK_DAMAGE,
            ALObjects.Attributes.ARROW_DAMAGE.get(),
            AttributesRegistry.PLAYER_FLY_MOVEMENT.get(),
            Attributes.FLYING_SPEED,
            ForgeMod.SWIM_SPEED.get());
    private static final List<Float> attributes_amount = List.of(
            3f,
            0.05f,
            0.5f,
            0.5f,
            1.0f);
    private static final List<AttributeModifier.Operation> attributes_operation = List.of(
            AttributeModifier.Operation.ADDITION,
            AttributeModifier.Operation.MULTIPLY_BASE,
            AttributeModifier.Operation.MULTIPLY_BASE,
            AttributeModifier.Operation.MULTIPLY_BASE,
            AttributeModifier.Operation.MULTIPLY_BASE);
    public TearlamentsModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingBreathInWater);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(this::ClearWaterFog);
        }
    }
    
    private void LivingBreathInWater(LivingBreatheEvent event) {
        if(event.canBreathe())return;
        LivingEntity entity = event.getEntity();
        if(entity.canDrownInFluidType(ForgeMod.WATER_TYPE.get()) && entity.getEyeInFluidType() == ForgeMod.WATER_TYPE.get())
        {
            if(TTMEntityUtils.hasModifier(entity, TTMModifierIds.tearlaments))
            {
                event.setCanBreathe(true);
                event.setCanRefillAir(true);
            }
        }
    }
    private void ClearWaterFog(ViewportEvent.RenderFog event)
    {
        Camera camera = event.getCamera();
        if(camera.getFluidInCamera() == FogType.WATER)
        {
            Entity cameraEntity = camera.getEntity();
            if(cameraEntity instanceof LivingEntity entity)
            {
                if(TTMEntityUtils.hasModifier(entity, TTMModifierIds.tearlaments))
                {
                    float waterVision = camera.getEntity() instanceof LocalPlayer player ? Math.max(0.25f, player.getWaterVision()) : 1.0f;
                    event.setFogShape(FogShape.CYLINDER);
                    event.setFarPlaneDistance(250.0f * waterVision);
                    event.setNearPlaneDistance(30.0f);
                    event.setCanceled(true);
                }
            }
        }
    }
    @Override
    public boolean cannightvision(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean isnightVision)
    {
        return context.getEntity().isEyeInFluidType(ForgeMod.WATER_TYPE.get());
    }
    @Override
    public float getnightvisionscale(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,float basescale,float scale)
    {
        return context.getEntity().isEyeInFluidType(ForgeMod.WATER_TYPE.get())?1.0f:scale;
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooks.INVENTORY_TICK,ModifierHooks.EQUIPMENT_CHANGE, ModifierHooksRegistry.NIGHT_VISION_HOOK);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        if(TTMEntityUtils.validArmorTool(tool,isCorrectSlot,livingEntity,itemStack))
        {
            int isinWater = livingEntity.isInWaterRainOrBubble()?1:0;
            EquipmentSlot slot = null;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(livingEntity.getItemBySlot(equipmentSlot) == itemStack)
                {
                    slot = equipmentSlot;
                    break;
                }
            }
            if(slot != null)
            {
                int level = modifier.getLevel();
                for(int i = 0; i < 5; i++) {
                    AttributeInstance instance = livingEntity.getAttribute(attributes.get(i));
                    if(instance != null)
                    {
                        AttributeModifier attributeModifier = this.createModifier(tool, isinWater, modifier, slot, level, i);
                        if (attributeModifier != null) {
                            instance.removeModifier(attributeModifier);
                            instance.addTransientModifier(attributeModifier);
                        }
                    }
                }
            }
        }
    }
    private double getAttributeNum(int level,int waterpercent,int index)
    {
        if(index <= 1)
        {
            return level * waterpercent * attributes_amount.get(index);
        }
        else
        {
            return waterpercent * attributes_amount.get(index);
        }
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool,int isinwater, ModifierEntry modifier, EquipmentSlot slot, int level, int index) {
        UUID uuid = this.getUUID(slot);
        if(isinwater == 0)return null;
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), getAttributeNum(level,isinwater,index), attributes_operation.get(index)) : null;
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player == null)return;
        int level = modifier.getLevel();
        list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.tearlaments.inwater")));
        for(int i = 0; i < 5 ;++i)
        {
            if(attributes_operation.get(i) == AttributeModifier.Operation.ADDITION)
            {
                TooltipModifierHook.addFlatBoost(modifier.getModifier(), Component.translatable(attributes.get(i).getDescriptionId()), getAttributeNum(level,1,i), list);
            }
            else
            {
                TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable(attributes.get(i).getDescriptionId()), getAttributeNum(level,1,i), list);
            }
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        UUID uuid = this.getUUID(context.getChangedSlot());
        if(uuid != null)
        {
            for(Attribute attribute : attributes) {
                AttributeInstance instance = context.getEntity().getAttribute(attribute);
                if(instance != null)
                {
                    instance.removeModifier(uuid);
                }
            }
        }
    }
}
