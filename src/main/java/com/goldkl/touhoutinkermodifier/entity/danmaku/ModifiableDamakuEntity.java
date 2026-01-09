package com.goldkl.touhoutinkermodifier.entity.danmaku;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

@SerialClass
public class ModifiableDamakuEntity extends ItemDanmakuEntity {
    @Nullable
    @SerialClass.SerialField
    private ItemStack tool;
    @SerialClass.SerialField
    private boolean hand;
    public ModifiableDamakuEntity(EntityType<? extends ModifiableDamakuEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ModifiableDamakuEntity(EntityType<? extends ModifiableDamakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public ModifiableDamakuEntity(EntityType<? extends ModifiableDamakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }
    public void setTool(ItemStack pStack, InteractionHand hand) {
        if(pStack.getItem() instanceof IModifiable) {
            this.stack = pStack.copy();
            this.hand = hand == InteractionHand.MAIN_HAND;
        }
    }
    @Override
    public float damage(Entity target) {
        float damage = super.damage(target);
        if(this.getOwner() instanceof LivingEntity attackerLiving && tool != null) {
            Player attackerPlayer = null;
            LivingEntity targetLiving = null;
            if (attackerLiving instanceof Player) {
                attackerPlayer = (Player) attackerLiving;
            }
            if(target instanceof LivingEntity) {
                targetLiving = (LivingEntity) target;
            }
            ToolStack tool = ToolStack.from(stack);
            ToolAttackContext.Builder builder = ToolAttackContext
                    .attacker(attackerLiving)
                    .target(target)
                    .hand(hand?InteractionHand.MAIN_HAND:InteractionHand.OFF_HAND)
                    .cooldown(1);
            if (hand) {
                builder.applyAttributes();
            } else {
                builder.toolAttributes(tool);
            }
            ToolAttackContext context = builder.build();
            float baseDamage = damage;
            MeleeDamagePercentModifierHook.DamageModifier damageModifier = new MeleeDamagePercentModifierHook.DamageModifier(baseDamage);
            List<ModifierEntry> modifiers = tool.getModifierList();
            for(ModifierEntry entry : modifiers) {
                damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
            }
            float originDamagefix = damage - baseDamage;
            damageModifier.addAdd(originDamagefix);
            for(ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooksRegistry.MELEE_DAMAGE_PERCENT).getMeleeDamageModifier(tool, entry, context, baseDamage, damage, damageModifier);
            }
            damage = damageModifier.getamount();
        }
        return damage;
    }
}
