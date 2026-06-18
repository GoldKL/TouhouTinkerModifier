package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityDodgeHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;

public class RendainoyakouModifier extends Modifier implements EntityDodgeHook, TooltipModifierHook {
    public RendainoyakouModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingEntityUnconsciousCooldownTick);
    }
    private void LivingEntityUnconsciousCooldownTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(TTMModifierIds.rendainoyakou);
            if(time > 0)
            {
                data.putInt(TTMModifierIds.rendainoyakou, time - 1);
            }
        });
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(ALObjects.Attributes.DODGE_CHANCE.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(TTMModifierIds.rendainoyakou).eachLevel(0.1f));
        hookBuilder.addHook(this, ModifierHooksRegistry.ENTITY_DODGE_HOOK,ModifierHooks.TOOLTIP);
    }
    @Override
    public void OnDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, @Nullable Entity attacker, @Nullable Entity directattacker){
        LivingEntity entity = context.getEntity();
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            if(data.getInt(TTMModifierIds.rendainoyakou) <= 0){
                int level = modifier.getLevel();
                int regen_level = level;
                if(SecretsealingclubModifier.SecretSealingClubcanUse(tool))
                {
                    regen_level += (int)Math.ceil(ToolEnergyCapability.getMaxEnergy(tool) / 100000.0);
                }
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,60, regen_level - 1));
                entity.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(),60, regen_level - 1));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,60, level - 1));
                data.putInt(TTMModifierIds.rendainoyakou, 200);
                if(entity instanceof Player player)
                {
                    TinkerNetwork.getInstance().sendTo(new SyncPersistentDataPacket(data.getCopy()), player);
                }
            }
        });

    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @org.jetbrains.annotations.Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player != null)
        {
            final int[] tick = {0};
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                tick[0] = data.getInt(TTMModifierIds.rendainoyakou);
            });
            int time = tick[0] / 20;
            if(tick[0] > 0)
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.rendainoyakou.cannot",time)));
            }
        }
    }
}
