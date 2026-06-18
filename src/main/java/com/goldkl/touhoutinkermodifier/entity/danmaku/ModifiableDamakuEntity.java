package com.goldkl.touhoutinkermodifier.entity.danmaku;

import com.goldkl.touhoutinkermodifier.api.DamageModifierDamageSource;
import com.goldkl.touhoutinkermodifier.helper.DamageModifier;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.capability.GrazeCapability;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.events.GeneralEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

@SerialClass
public class ModifiableDamakuEntity extends ItemDanmakuEntity {
    @Nullable
    @SerialClass.SerialField
    private ItemStack tool;
    @SerialClass.SerialField
    private boolean hand;
    private DamageModifier modifier;
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
        if(TTMItemUtils.isModifiable(pStack)) {
            this.tool = pStack.copy();
            this.hand = hand == InteractionHand.MAIN_HAND;
        }
    }
    @Override
    public float damage(Entity target) {
        float damage = super.damage(target);
        if(this.getOwner() instanceof LivingEntity attackerLiving && tool != null) {
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
            DamageModifier damageModifier = new DamageModifier(baseDamage);
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
            modifier = damageModifier;
        }
        return damage;
    }
    @Override
    public void hurtTarget(EntityHitResult result) {
        if (!this.self().level().isClientSide) {
            Entity e = result.getEntity();
            DamageSource source = this.source();
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)e;
                if (le.hurtTime > 0 && (YHModConfig.COMMON.invulFrameForDanmaku.get() || e instanceof Player || e instanceof YoukaiEntity)) {
                    DamageSource last = le.getLastDamageSource();
                    if (last != null && last.getDirectEntity() instanceof IYHDanmaku) {
                        return;
                    }
                }
            }

            LivingEntity target;
            PartEntity<?> pe;
            for(target = null; e instanceof PartEntity; e = pe.getParent()) {
                pe = (PartEntity)e;
            }

            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity)e;
                target = le;
            }

            Entity owner = this.self().getOwner();
            if (target != null && owner instanceof YoukaiEntity) {
                YoukaiEntity youkai = (YoukaiEntity)owner;
                youkai.danmakuHitTarget(this, source, target);
            } else {
                if (owner instanceof Player) {
                    Player player = (Player)owner;
                    if (e instanceof LivingEntity) {
                        LivingEntity le = (LivingEntity)e;
                        if (!(GrazeCapability.HOLDER.get(player)).shouldHurt(le)) {
                            return;
                        }
                    }
                }
                float dmg = this.damage(e);
                if(this.modifier != null){
                    ((DamageModifierDamageSource)source).touhouTinkerModifier$setDamageModifier(this.modifier);
                }
                e.hurt(source, this.damage(e));
            }
        }
    }
}
