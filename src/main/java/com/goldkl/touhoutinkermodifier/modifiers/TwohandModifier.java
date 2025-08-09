package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

public class TwohandModifier extends Modifier implements MeleeHitModifierHook{
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
    UUID uuid = UUID.fromString("d2ab3741-d1ad-4e3e-afa2-f37f1aac9cf1");
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt)
    {
        /*Player player = context.getPlayerAttacker();
        if(player != null)
        {
            TouhouTinkerModifier.LOGGER.info("{}",((PlayerAttackProperties)player).getComboCount());
        }*/
        LivingEntity entity = context.getLivingTarget();
        if(entity != null) {
            AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
            if (instance != null) {
                double basevalue = 0;
                AttributeModifier temp = instance.getModifier(uuid);
                if(temp != null) {
                    basevalue = temp.getAmount();
                }
                instance.removeModifier(uuid);
                instance.addPermanentModifier(new AttributeModifier(uuid,"reducemaxhp",basevalue - damageDealt, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}
