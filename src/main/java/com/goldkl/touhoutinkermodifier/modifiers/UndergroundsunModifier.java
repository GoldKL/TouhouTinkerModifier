package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.EffectApplicableModifierHook;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.network.SyncPersistentDataPacket;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.tools.modules.armor.CounterModule;

import java.util.List;

public class UndergroundsunModifier extends Modifier implements OnAttackedModifierHook, EffectApplicableModifierHook, InventoryTickModifierHook, TooltipModifierHook {
    //地底太阳：灵乌路空
    public UndergroundsunModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingEntityUndergroundsunModifierCooldownTick);
    }
    private void LivingEntityUndergroundsunModifierCooldownTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.isAlive())return;
        entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
            int time = data.getInt(TTMModifierIds.undergroundsun);
            if(time > 0)
            {
                data.putInt(TTMModifierIds.undergroundsun, time - 1);
            }
        });
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED, EtSTLibHooks.EFFECT_APPLICABLE, ModifierHooks.INVENTORY_TICK, ModifierHooks.TOOLTIP);
    }
    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (isDirectDamage && tool.hasTag(TinkerTags.Items.ARMOR) && source.getEntity() instanceof LivingEntity attacker) {
            LivingEntity defender = context.getEntity();
            int level = (int) CounterModule.getLevel(tool, modifier, slotType, defender);
            if (RANDOM.nextFloat() < (level * 0.25f)) {
                attacker.addEffect(new MobEffectInstance(MobeffectRegistry.RADIATION.get(), 40 + 20 * RANDOM.nextInt(level + 3), level - 1), defender);
            }
        }
    }
    @Override
    public boolean isApplicable(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, MobEffectInstance instance, Boolean notApplicable) {
        return notApplicable||instance.getEffect() == MobeffectRegistry.RADIATION.get();
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide)return;
        boolean flag = TTMEntityUtils.validArmorTool(tool,isCorrectSlot,holder,stack);
        boolean berserk = holder.getMaxHealth() * 0.3f >= holder.getHealth();
        ModDataNBT dataNBT = holder.getCapability(PersistentDataCapability.CAPABILITY).orElse(null);
        boolean canuse = false;
        if(dataNBT != null)
        {
            canuse = dataNBT.getInt(TTMModifierIds.undergroundsun) == 0;
        }
        if(flag && berserk && canuse)
        {
            int level = TTMEntityUtils.getModifiermaxLevel(holder, TTMModifierIds.undergroundsun);
            double radius = 3.0 + level;
            List<Entity> list = world.getEntities(holder, holder.getBoundingBox().inflate(radius));
            for(Entity entity : list) {
                if(entity == holder)continue;
                if (entity instanceof LivingEntity) {
                    entity.hurt(world.damageSources().mobAttack(holder), level * 6);
                    double d0 = entity.getX() - holder.getX();
                    double d1 = entity.getZ() - holder.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                    entity.push(d0 / d2 * (double)1.5F, 0.15, d1 / d2 * (double)1.5F);
                }
            }
            MagicManager.spawnParticles(world, new BlastwaveParticleOptions(SchoolRegistry.FIRE.get().getTargetingColor(), (float) radius), holder.getX(), holder.getY() + .165f, holder.getZ(), 1, 0, 0, 0, 0, true);
            dataNBT.putInt(TTMModifierIds.undergroundsun, 400);
            if(holder instanceof Player player)
                TinkerNetwork.getInstance().sendTo(new SyncPersistentDataPacket(dataNBT.getCopy()), player);
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player != null)
        {
            final int[] tick = {0};
            player.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                tick[0] = data.getInt(TTMModifierIds.undergroundsun);
            });
            int time = tick[0] / 20;
            if(tick[0] == 0)
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.undergroundsun.can")));
            }
            else
            {
                list.add(modifier.getModifier().applyStyle(Component.translatable("modifier.touhoutinkermodifier.undergroundsun.cannot",time)));
            }
        }
    }
}
