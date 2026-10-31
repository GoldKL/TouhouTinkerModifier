package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class UnendingDespairModifier extends Modifier implements ProjectileLaunchModifierHook, MeleeDamagePercentModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooksRegistry.MELEE_DAMAGE_PERCENT);

    }
    private double getLostHealth(LivingEntity entity){
        return entity.getMaxHealth() - entity.getHealth();
    }
    @Override
    public void onProjectileLaunch(IToolStackView iToolStackView, ModifierEntry modifierEntry, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, ModDataNBT modDataNBT, boolean b) {
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if(arrow != null)
        {
            double lostHealth = getLostHealth(shooter);
            int level = modifier.getLevel();
            arrow.setBaseDamage(arrow.getBaseDamage() + level * 0.3 * lostHealth);
        }
    }
    @Override
    public void getMeleeDamageModifier(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage, DamageModifier damagemodifier)
    {
        LivingEntity attacker = context.getAttacker();
        int level = modifier.getLevel();
        float lostHealth = (float) getLostHealth(attacker);
        damagemodifier.addAdd(level * 0.5f * lostHealth);
    }
}
