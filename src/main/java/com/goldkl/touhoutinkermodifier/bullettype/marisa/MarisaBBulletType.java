package com.goldkl.touhoutinkermodifier.bullettype.marisa;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.entity.danmaku.ModifiableDamakuEntity;
import com.goldkl.touhoutinkermodifier.helper.RenderingHelper;
import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MarisaBBulletType extends AbstractMarisaBulletType {
    public String getTypeName()
    {
        return "marisa_type_b_bullet";
    }
    public static final ResourceLocation Key_Marisa_Laser = TouhouTinkerModifier.getResource("key_marisa_laser");

    @Override
    public boolean isShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        return remainingTicks % 10 == 0;
    }
    @Override
    public void startShootBullet(Level level, LivingEntity entity, ItemStack itemStack)
    {
        super.startShootBullet(level, entity, itemStack);
        if(level.isClientSide) {
            entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                int num = Math.max(0,data.getInt(RenderingHelper.KEY_NEED_TO_RENDER));
                data.putInt(RenderingHelper.KEY_NEED_TO_RENDER, num + 1);
                data.putBoolean(Key_Marisa_Laser, true);
            });
        }
    }
    @Override
    public void stopShootBullet(Level level, LivingEntity entity, ItemStack itemStack,int remainingTicks)
    {
        super.stopShootBullet(level, entity, itemStack,remainingTicks);
        if(level.isClientSide) {
            entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                int num = data.getInt(RenderingHelper.KEY_NEED_TO_RENDER) - 1;
                if(num <= 0)
                {
                    data.remove(RenderingHelper.KEY_NEED_TO_RENDER);
                }
                else data.putInt(RenderingHelper.KEY_NEED_TO_RENDER,num);
                data.remove(Key_Marisa_Laser);
            });
        }
    }
    @Override
    public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if(!level.isClientSide)
        {
            int power = TTMEntityUtils.getPowerValue(entity);
            if(remainingTicks % 20 == 0)
                this.MarisaCommonShootBulletTick(level, entity, itemStack, remainingTicks);
            if(power > 0)
            {
                float tempdamage = power * 2f;
                Vec3 base_position = new Vec3(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                Vec3 fix_position = RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 1.5);
                base_position = base_position
                        .add(fix_position);
                Vec3 start = base_position;
                Vec3 end = entity.getLookAngle().normalize().scale(40).add(start);
                end = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getLocation();
                AABB range = entity.getBoundingBox().move(fix_position).expandTowards(end.subtract(start));
                List<HitResult> hits = new ArrayList<>();
                for(Entity target : level.getEntities(entity, range, entity1 -> entity1.isPickable() && entity1.isAlive())) {
                    HitResult hit = Utils.checkEntityIntersecting(target, start, end, 0.15f);
                    if (hit.getType() == HitResult.Type.ENTITY) {
                        hits.add(hit);
                    }
                }
                if (!hits.isEmpty()) {
                    hits.sort(Comparator.comparingDouble((o) -> o.getLocation().distanceToSqr(start)));
                    for(HitResult hit : hits) {
                        DamageSource damageSource = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(YHDamageTypes.DANMAKU), entity, entity);
                        Entity target = ((EntityHitResult) hit).getEntity();
                        float damage = tempdamage;
                        if(itemStack.getItem() instanceof IModifiable)
                        {
                            ToolStack tool = ToolStack.from(itemStack);
                            ToolAttackContext.Builder builder = ToolAttackContext
                                    .attacker(entity)
                                    .target(target)
                                    .hand(entity.getUsedItemHand())
                                    .cooldown(1);
                            ToolAttackContext context = builder.build();
                            if (entity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                                builder.applyAttributes();
                            } else {
                                builder.toolAttributes(tool);
                            }
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
                        target.hurt(damageSource,damage);
                    }
                }
            }
        }
    }
}
