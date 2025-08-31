package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


import java.util.function.BiFunction;

public class RainbowtaichiModifier extends Modifier implements ModifyDamageModifierHook, OnAttackedModifierHook, MeleeDamagePercentModifierHook, ProjectileLaunchModifierHook {//MeleeDamageModifierHook,
    //虹色太极：红美铃
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.rainbowtaichi);
    public RainbowtaichiModifier()
    {
        MinecraftForge.EVENT_BUS.addListener(this::LivingBreath);
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooks.ON_ATTACKED, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);//ModifierHooks.MELEE_DAMAGE
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }
    private static int getlevel(LivingEntity entity, BiFunction<Integer,Integer,Integer> function)
    {
        int level = 0;
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            level = function.apply(level,tool.getModifierLevel(ModifierIds.rainbowtaichi));
        }
        return level;
    }
    private final static int MELLEE_AIRCOST = 50;
    private final static int PROJECTILE_AIRCOST = 50;
    private final static double ATTACKED_AIR_GET = 1.5;
    private final static BiFunction<Integer,Integer,Integer>MAXLEVEL = Math::max;
    private final static BiFunction<Integer,Integer,Integer>TOTALLEVEL = Integer::sum;
    private void LivingBreath(LivingBreatheEvent event) {
        LivingEntity entity = event.getEntity();
        int level = getlevel(entity, MAXLEVEL);
        if(level > 0)
        {
            event.setRefillAirAmount(event.getRefillAirAmount() + level * 2);
        }
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity attacker = context.getAttacker();
        if(attacker.getAirSupply() >= MELLEE_AIRCOST)
        {
            int level = getlevel(attacker, TOTALLEVEL);
            if(level > 0)
            {
                attacker.setAirSupply(Math.max(0,attacker.getAirSupply() - MELLEE_AIRCOST));
                damagemodifier.addPercent(level * 0.1f);
            }
        }
    }
    /*@Override
    public float getMeleePercent(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, float percent)
    {
        LivingEntity attacker = context.getAttacker();
        if(attacker.getAirSupply() >= MELLEE_AIRCOST)
        {
            int level = getlevel(attacker, TOTALLEVEL);
            if(level > 0)
            {
                attacker.setAirSupply(Math.max(0,attacker.getAirSupply() - MELLEE_AIRCOST));
                percent += level * 0.1f;
            }
        }
        return percent;
    }*/
    /*@Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        if(attacker.getAirSupply() >= MELLEE_AIRCOST)
        {
            int level = getlevel(attacker, TOTALLEVEL);
            if(level > 0)
            {
                attacker.setAirSupply(Math.max(0,attacker.getAirSupply() - MELLEE_AIRCOST));
                damage += baseDamage * level * 0.1f;
            }
        }
        return damage;
    }*/
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(source.is(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER))return amount;
        LivingEntity entity = context.getEntity();
        if(entity.getAirSupply() > 0 && SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType))
        {
            int level = getlevel(entity, MAXLEVEL);
            if(level > 0)
            {
                if(amount <= level + 1 && entity.getAirSupply() >= entity.getMaxAirSupply() / 2)
                {
                    return 0;
                }
                int aircost = Math.max(1,15 - level);
                int getcostair = Math.min(entity.getAirSupply(),(int)Math.ceil(amount * aircost));
                int reduceamount = getcostair /aircost;
                int gettruecostair = reduceamount * aircost;
                if(reduceamount > 0)
                {
                    amount -= (float) reduceamount;
                    entity.setAirSupply(Math.max(0, entity.getAirSupply() - gettruecostair));
                }
            }
        }
        return amount;
    }
    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (slotType.getType() == EquipmentSlot.Type.HAND && entity.getAirSupply() < entity.getMaxAirSupply()) {
            int level = getlevel(entity, MAXLEVEL);
            double air = amount * level * ATTACKED_AIR_GET / (isDirectDamage?1.0:2.0);
            entity.setAirSupply(Math.max(entity.getMaxAirSupply(),(int) Math.ceil(entity.getAirSupply() + air)));
        }
    }

    @Override
    public void onProjectileLaunch(IToolStackView iToolStackView, ModifierEntry modifierEntry, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, ModDataNBT modDataNBT, boolean b) {
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if(shooter.getAirSupply() >= PROJECTILE_AIRCOST && arrow != null)
        {
            int level = getlevel(shooter, TOTALLEVEL);
            if(level > 0)
            {
                arrow.setBaseDamage(arrow.getBaseDamage() + level * 2.0);
                shooter.setAirSupply(Math.max(0,shooter.getAirSupply() - PROJECTILE_AIRCOST));
            }
        }
    }
}
