package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class TearlamentsModifier extends Modifier implements InventoryTickModifierHook, EquipmentChangeModifierHook {
    //珠泪哀歌：若鹭姬
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.tearlaments);
    public static final TinkerDataCapability.TinkerDataKey<PlayerStat> IsinWater = TinkerDataCapability.ComputableDataKey.of(ModifierIds.tearlaments.withSuffix("_isinwater"), PlayerStat::new);
    private static final UUID uuid = UUID.nameUUIDFromBytes((ModifierIds.tearlaments.toString()).getBytes());
    final SlotInChargeModule SICM;
    public TearlamentsModifier()
    {
        SICM = new SlotInChargeModule(SLOT_IN_CHARGE);
        MinecraftForge.EVENT_BUS.addListener(this::LivingBreathInWater);
        MinecraftForge.EVENT_BUS.addListener(this::ClearWater);
    }
    
    private void LivingBreathInWater(LivingBreatheEvent event) {
        if(event.canBreathe())return;
        LivingEntity entity = event.getEntity();
        if(entity.canDrownInFluidType(ForgeMod.WATER_TYPE.get()) && entity.getEyeInFluidType() == ForgeMod.WATER_TYPE.get())
        {
            boolean check = false;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
                ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
                if(tool.getModifier(ModifierIds.tearlaments)!=ModifierEntry.EMPTY)
                {
                    check = true;
                    break;
                }
            }
            if(check)
            {
                event.setCanBreathe(true);
                event.setCanRefillAir(true);
                event.setConsumeAirAmount(0);
                event.setRefillAirAmount(4);
            }
        }
    }
    private void ClearWater(ViewportEvent.RenderFog event)
    {
        Camera camera = event.getCamera();
        if(camera.getFluidInCamera() == FogType.WATER)
        {
            Entity cameraEntity = camera.getEntity();
            if(cameraEntity instanceof LivingEntity entity)
            {
                boolean check = false;
                for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
                {
                    if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
                    ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
                    if(tool.getModifier(ModifierIds.tearlaments)!=ModifierEntry.EMPTY)
                    {
                        check = true;
                        break;
                    }
                }
                if(check)
                {
                    float waterVision = camera.getEntity() instanceof LocalPlayer player ? Math.max(0.25f, player.getWaterVision()) : 1.0f;
                    event.setFogShape(FogShape.CYLINDER);
                    event.setFarPlaneDistance(250.0f * waterVision);
                    event.setNearPlaneDistance(-8.0f);
                    event.setCanceled(true);
                }
            }
        }
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.EQUIPMENT_CHANGE);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        updatavalue(livingEntity);
        if(isCorrectSlot)
        {
            final boolean[] state = {livingEntity.isInWater(),false};
            livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                PlayerStat oldstate = (PlayerStat)data.get(IsinWater);
                if(oldstate != null)
                {
                    state[1] = oldstate.playerStat;
                }
            });
            if(state[0])
            {
                //现在在水里
                if(livingEntity instanceof Player player)
                {
                    player.getAbilities().flying = true;
                    player.onUpdateAbilities();
                }
            }
            else if(state[1])
            {
                //之前在水里现在不在水里
                if(livingEntity instanceof Player player)
                {
                    player.getAbilities().flying = player.isCreative()||player.isSpectator();
                    player.onUpdateAbilities();
                }
            }
            livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                data.put(IsinWater,new PlayerStat(state[0]));
            });
        }
    }
    void updatavalue(LivingEntity entity)
    {
        int level = 0;
        int isinWater = entity.isInWaterRainOrBubble()?1:0;
        for(EquipmentSlot slot : EquipmentSlot.values())
        {
            level = SlotInChargeModule.getLevel(entity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot);
            if(level != 0)
                break;
        }
        AttributeInstance instance = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance != null) {
            instance.removeModifier(uuid);
            float attributeValue = level * isinWater * 3;
            if (attributeValue != 0) {
                instance.addTransientModifier(new AttributeModifier(uuid, ModifierIds.tearlaments.toString(), attributeValue, AttributeModifier.Operation.ADDITION));
            }
        }
    }
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context)
    {
        SICM.onEquip(tool, modifier, context);
        if(!context.getEntity().level().isClientSide)
            updatavalue(context.getEntity());
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context)
    {
        SICM.onUnequip(tool, modifier, context);
        if(!context.getEntity().level().isClientSide)
        {
            updatavalue(context.getEntity());
            LivingEntity entity = context.getEntity();
            if(entity instanceof Player player)
            {
                boolean check = false;
                for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
                {
                    if(SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, equipmentSlot))
                    {
                        check = true;
                        break;
                    }
                }
                if(!check)
                {
                    player.getAbilities().flying = player.isCreative()||player.isSpectator();
                    player.onUpdateAbilities();
                }
            }
        }
    }
    public static class PlayerStat{
        boolean playerStat;
        PlayerStat(){
            playerStat = false;
        }
        PlayerStat(boolean playerStat){
            this.playerStat = playerStat;
        }
        boolean getPlayerStat(){
            return playerStat;
        }
        void setPlayerStat(boolean playerStat){
            this.playerStat = playerStat;
        }
    }
}
