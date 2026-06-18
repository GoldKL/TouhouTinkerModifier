package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.c2h6s.etstlib.tool.hooks.OnHoldingPreventDeathHook;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalTeleporter;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.special.CapacityBarHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class YakumohypothesisModifier extends NoLevelsModifier implements OnHoldingPreventDeathHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PREVENT_DEATH);
    }
    @Override
    public float onHoldingPreventDeath(LivingEntity entity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
        Level world = entity.level();
        if(entity instanceof ServerPlayer serverPlayer && !(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) && !serverPlayer.hasEffect(MobeffectRegistry.SUKIMA.get()))
        {
            //此处使用了铁魔法“回溯”的代码
            ServerLevel respawnLevel = ((ServerLevel) world).getServer().getLevel(serverPlayer.getRespawnDimension());
            respawnLevel = respawnLevel == null ? world.getServer().overworld() : respawnLevel;
            var spawnLocation = RecallSpell.findSpawnPosition(respawnLevel, serverPlayer);
            //IronsSpellbooks.LOGGER.debug("Recall.onCast findSpawnLocation: {}", spawnLocation);
            if (spawnLocation.isPresent()) {
                Vec3 vec3 = spawnLocation.get();
                //IronsSpellbooks.LOGGER.debug("Recall.onCast.a dimension: {} -> {}", serverPlayer.level.dimension(), respawnLevel.dimension());
                if (serverPlayer.level().dimension() != respawnLevel.dimension()) {
                    serverPlayer.changeDimension(respawnLevel, new PortalTeleporter(vec3));
                } else {
                    serverPlayer.teleportTo(vec3.x, vec3.y, vec3.z);
                }
            } else {
                respawnLevel = world.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("ttf","gap")));
                if(respawnLevel == null)respawnLevel = world.getServer().overworld();
                //IronsSpellbooks.LOGGER.debug("Recall.onCast.b dimension: {} -> {}", serverPlayer.level.dimension(), respawnLevel.dimension());
                if (serverPlayer.level().dimension() != respawnLevel.dimension()) {
                    serverPlayer.changeDimension(respawnLevel, new PortalTeleporter(Vec3.ZERO));
                }
                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
                if(respawnLevel.dimension().equals(Level.OVERWORLD))
                {
                    var pos = respawnLevel.getSharedSpawnPos();
                    serverPlayer.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                }
                else {
                    serverPlayer.teleportTo(0, -52, 0);
                }
            }
            entity.addEffect(new MobEffectInstance(MobeffectRegistry.SUKIMA.get(), 36000, 0));
            return entity.getMaxHealth();
        }
        return 0;
    }
    /*旧实现，通过阻挡伤害来实现
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK);
    }

    @Override
    public boolean isDamageBlocked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount) {
        LivingEntity entity = equipmentContext.getEntity();
        Level world = entity.level();
        if(entity instanceof ServerPlayer serverPlayer && !(damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) && !serverPlayer.hasEffect(MobeffectRegistry.SUKIMA.get()) && amount >= serverPlayer.getHealth())
        {
            //此处使用了铁魔法“回溯”的代码
            ServerLevel respawnLevel = ((ServerLevel) world).getServer().getLevel(serverPlayer.getRespawnDimension());
            respawnLevel = respawnLevel == null ? world.getServer().overworld() : respawnLevel;
            var spawnLocation = RecallSpell.findSpawnPosition(respawnLevel, serverPlayer);
            //IronsSpellbooks.LOGGER.debug("Recall.onCast findSpawnLocation: {}", spawnLocation);
            if (spawnLocation.isPresent()) {
                Vec3 vec3 = spawnLocation.get();
                //IronsSpellbooks.LOGGER.debug("Recall.onCast.a dimension: {} -> {}", serverPlayer.level.dimension(), respawnLevel.dimension());
                if (serverPlayer.level().dimension() != respawnLevel.dimension()) {
                    serverPlayer.changeDimension(respawnLevel, new PortalTeleporter(vec3));
                } else {
                    serverPlayer.teleportTo(vec3.x, vec3.y, vec3.z);
                }
            } else {
                respawnLevel = world.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("ttf","gap")));
                if(respawnLevel == null)respawnLevel = world.getServer().overworld();
                //IronsSpellbooks.LOGGER.debug("Recall.onCast.b dimension: {} -> {}", serverPlayer.level.dimension(), respawnLevel.dimension());
                if (serverPlayer.level().dimension() != respawnLevel.dimension()) {
                    serverPlayer.changeDimension(respawnLevel, new PortalTeleporter(Vec3.ZERO));
                }
                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
                if(respawnLevel.dimension().equals(Level.OVERWORLD))
                {
                    var pos = respawnLevel.getSharedSpawnPos();
                    serverPlayer.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                }
                else {
                    serverPlayer.teleportTo(0, -52, 0);
                }
            }
            entity.addEffect(new MobEffectInstance(MobeffectRegistry.SUKIMA.get(), 36000, 0));
            return true;
        }
        return false;
    }*/
}
