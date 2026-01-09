package com.goldkl.touhoutinkermodifier.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

public class KillerdollModifier extends NoLevelsModifier implements ProjectileHitModifierHook {
    //杀人玩偶：十六夜咲夜
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }
    @Override
    public int getPriority() {
        return 25;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if(attacker != null && target != null) {
            ItemStack itemStack = attacker.getOffhandItem();
            if(itemStack.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(attacker.getOffhandItem());
                if(!tool.isBroken() && tool.hasTag(TinkerTags.Items.MELEE))
                {
                    ToolAttackContext.Builder builder = ToolAttackContext
                            .attacker(attacker)
                            .target(target)
                            .hand(InteractionHand.OFF_HAND)
                            .cooldown(1)
                            .toolAttributes(tool);
                    ToolAttackUtil.performAttack(tool,builder.build());
                    projectile.discard();//考虑直接销毁这个弹射物，不确定会不会引发bug

                    return true;
                }
            }
        }
        return notBlocked;
    }
}
