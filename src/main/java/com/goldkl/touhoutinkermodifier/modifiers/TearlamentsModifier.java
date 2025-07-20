package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.NightVisionHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.mojang.blaze3d.shaders.FogShape;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;

public class TearlamentsModifier extends Modifier implements InventoryTickModifierHook, EquipmentChangeModifierHook, NightVisionHook {
    //珠泪哀歌：若鹭姬
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.tearlaments);
    public static final TinkerDataCapability.TinkerDataKey<TTMEntityUtils.BooleanStat> IsinWater = TinkerDataCapability.ComputableDataKey.of(ModifierIds.tearlaments.withSuffix("_isinwater"), TTMEntityUtils.BooleanStat::new);
    private static final UUID uuid = UUID.nameUUIDFromBytes((ModifierIds.tearlaments.toString()).getBytes());
    final SlotInChargeModule SICM;
    public TearlamentsModifier()
    {
        SICM = new SlotInChargeModule(SLOT_IN_CHARGE);
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
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.EQUIPMENT_CHANGE, ModifierHooksRegistry.NIGHT_VISION_HOOK);
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level world, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if(world.isClientSide())return;
        if(isCorrectSlot)
        {
            EquipmentSlot slot = null;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                if(livingEntity.getItemBySlot(equipmentSlot) == itemStack)
                {
                    slot = equipmentSlot;
                    break;
                }
            }
            if(slot!=null && SlotInChargeModule.isInCharge(livingEntity.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot))
            {
                updatavalue(livingEntity);
                final boolean[] state = {livingEntity.isInWater(),false};
                livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                    TTMEntityUtils.BooleanStat oldstate = (TTMEntityUtils.BooleanStat)data.get(IsinWater);
                    if(oldstate != null)
                    {
                        state[1] = oldstate.getBooleanstat();
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
                        if(!(player.isCreative()||player.isSpectator()))
                        {
                            player.getAbilities().flying = player.getAbilities().mayfly;
                            player.onUpdateAbilities();
                        }
                        player.getAbilities().flying = player.isCreative()||player.isSpectator();
                        player.onUpdateAbilities();
                    }
                }
                livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
                    TTMEntityUtils.BooleanStat oldstate = (TTMEntityUtils.BooleanStat)data.get(IsinWater);
                    if(oldstate != null)
                    {
                        oldstate.setBooleanstat(state[0]);
                    }
                    else
                    {
                        data.put(IsinWater,new TTMEntityUtils.BooleanStat(state[0]));
                    }
                });
            }
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
        AttributeInstance instance2 = entity.getAttribute(ALObjects.Attributes.ARROW_DAMAGE.get());
        if (instance2 != null) {
            instance2.removeModifier(uuid);
            float attributeValue = level * isinWater * 0.05f;
            if (attributeValue != 0) {
                instance2.addTransientModifier(new AttributeModifier(uuid, ModifierIds.tearlaments.toString(), attributeValue, AttributeModifier.Operation.MULTIPLY_BASE));
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
                /*if(!check&&!(player.isCreative()||player.isSpectator()))
                {
                    player.getAbilities().flying = player.getAbilities().mayfly;
                    player.onUpdateAbilities();
                }*/
            }
        }
    }

}
