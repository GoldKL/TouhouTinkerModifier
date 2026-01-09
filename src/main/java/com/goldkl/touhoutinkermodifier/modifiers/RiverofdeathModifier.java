package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.OnHoldingPreventDeathHook;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.helper.compat.TouhouLittleMaidHelper;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMModListUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.special.CapacityBarHook;
import slimeknights.tconstruct.library.modifiers.modules.capacity.CapacityBarModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.CapacityBarValidator;
import slimeknights.tconstruct.library.modifiers.modules.capacity.DurabilityShieldModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RiverofdeathModifier extends Modifier implements ToolStatsModifierHook, MeleeHitModifierHook, OnHoldingPreventDeathHook, ProjectileHitModifierHook, ProjectileLaunchModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new CapacityBarModule(TTMModifierIds.riverofdeath,LevelingInt.flat(500), null));
        hookBuilder.addModule(new DurabilityShieldModule(0x8b0000));
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.MELEE_HIT,EtSTLibHooks.PREVENT_DEATH, ModifierHooks.PROJECTILE_HIT, ModifierHooks.PROJECTILE_LAUNCH);
    }
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary){};
    private static final int Shield_coefficient = 3;
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if(arrow != null && arrow.isCritArrow() && primary) {
            IToolUuidGetter.getUuidForTool(tool).ifPresent(uuid->{
                CompoundTag tag = new CompoundTag();
                tag.putUUID(TTMModifierIds.riverofdeath.toString(),uuid);
                persistentData.put(TTMModifierIds.riverofdeath,tag);
            });
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target,boolean notBlocked) {
        if(notBlocked && target != null && attacker != null && !attacker.level().isClientSide())
        {
            if(persistentData.getCompound(TTMModifierIds.riverofdeath).hasUUID(TTMModifierIds.riverofdeath.toString()))
            {
                UUID uuid = persistentData.getCompound(TTMModifierIds.riverofdeath).getUUID(TTMModifierIds.riverofdeath.toString());
                Predicate<ItemStack> condition = itemStack -> {
                    if(itemStack.getItem() instanceof IModifiable)
                    {
                        ToolStack tool = ToolStack.from(itemStack);
                        if(IToolUuidGetter.getUuidForTool(tool).isPresent())
                        {
                            return uuid.equals(IToolUuidGetter.getUuidForTool(tool).get());
                        }
                    }
                    return false;
                };
                Consumer<ItemStack> consumer = itemStack -> {
                    ToolStack tool = ToolStack.from(itemStack);
                    int level = modifier.getLevel();
                    CapacityBarHook bar = modifier.getHook(ModifierHooks.CAPACITY_BAR);
                    bar.addAmount(tool,modifier,Shield_coefficient * level);
                };
                boolean has_done = false;
                for(EquipmentSlot slot:EquipmentSlot.values())
                {
                    ItemStack itemStack = attacker.getItemBySlot(slot);
                    if (condition.test(itemStack)) {
                        consumer.accept(itemStack);
                        has_done = true;
                        break;
                    }
                }
                if(!has_done)
                {
                    if (attacker instanceof Player player) {
                        Inventory inventory = player.getInventory();
                        for(int i = 0; i < inventory.getContainerSize(); ++i) {
                            ItemStack stack = inventory.getItem(i);
                            if (condition.test(stack)) {
                                consumer.accept(stack);
                                break;
                            }
                        }
                    }
                    else if(TTMModListUtil.TouhouLittleMaidLoaded)
                    {
                        TouhouLittleMaidHelper.TraverseMaidBackPack(attacker, condition, consumer,true);
                    }
                }
            }
        }
        return !notBlocked;
    }

    @Override
    public int getPriority() {
        return 125;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.isFullyCharged() && context.isCritical() && damageDealt > 0) {
            int level = modifier.getLevel();
            CapacityBarHook bar = modifier.getHook(ModifierHooks.CAPACITY_BAR);
            bar.addAmount(tool,modifier,Shield_coefficient * level);
        }
    }
    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @javax.annotation.Nullable RegistryAccess access) {
        return getHook(ModifierHooks.DISPLAY_NAME).getDisplayName(tool, entry, entry.getDisplayName(), access);
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int blood = context.getPersistentData().getInt(TTMModifierIds.riverofdeath);
        float level = modifier.getEffectiveLevel();
        float num = level * blood / 200;
        if(blood > 0)
        {
            if (context.hasTag(TinkerTags.Items.MELEE)) {
                ToolStats.ATTACK_DAMAGE.add(builder, num);
            }
            if (context.hasTag(TinkerTags.Items.HARVEST)) {
                ToolStats.MINING_SPEED.add(builder, num);
            }
            if (context.hasTag(TinkerTags.Items.ARMOR)) {
                ToolStats.ARMOR.add(builder, num);
            }
            if (context.hasTag(TinkerTags.Items.RANGED)) {
                ToolStats.PROJECTILE_DAMAGE.add(builder, num);
            }
        }
    }

    public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
        CapacityBarHook bar = modifier.getHook(ModifierHooks.CAPACITY_BAR);
        if(!source.is(TagsRegistry.DamageTypeTag.PASS_WORLD_ENDER) && bar.getAmount(tool) >= 0.9 * bar.getCapacity(tool, modifier))
        {
            float recover = livingEntity.getMaxHealth() * 0.5f;
            if(recover > 0)
            {
                bar.setAmount(tool,modifier,0);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                return recover;
            }
        }
        return 0;
    }
}
