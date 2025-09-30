package com.goldkl.touhoutinkermodifier.bullettype;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.entity.danmaku.ModifiableDamakuEntity;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Function;

public class DefaultBulletType extends AbstractBulletType {
    public String getTypeName()
    {
        return TYPENAME;
    }
    public static final String TYPENAME = "common_bullet";
    public static final ResourceLocation ID = TouhouTinkerModifier.getResource(TYPENAME);
    @Override
    public double getHitboxFix() {
        return 0;
    }
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    @Override
    public boolean isShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        return remainingTicks % 20 == 0;
    }

    @Override
    public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if(!level.isClientSide)
        {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            int power = TTMEntityUtils.getPowerValue(entity);
            ModifiableDamakuEntity danmaku = new ModifiableDamakuEntity(EntitiesRegistry.ModifiableDanmaku.get(), entity, level);
            danmaku.setTool(itemStack, entity.getUsedItemHand());
            danmaku.setItem(YHDanmaku.Bullet.BALL.get(DyeColor.RED).asStack());
            danmaku.setup(
                    YHDanmaku.Bullet.BALL.damage(),
                    40,
                    false,
                    YHDanmaku.Bullet.BALL.bypass(),
                    RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 2)
            );
            level.addFreshEntity(danmaku);
            float midpoint = power/2.0f;
            float angle = 20f;
            for(int i = 0; i < power; i++)
            {
                ModifiableDamakuEntity vicedanmaku = new ModifiableDamakuEntity(EntitiesRegistry.ModifiableDanmaku.get(), entity, level);
                vicedanmaku.setTool(itemStack, entity.getUsedItemHand());
                vicedanmaku.setItem(YHDanmaku.Bullet.BALL.get(DyeColor.GRAY).asStack());
                vicedanmaku.setPos(vicedanmaku.position()
                        .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (i - midpoint + 0.5f) * angle, 3)));
                vicedanmaku.setup(
                        YHDanmaku.Bullet.BALL.damage(),
                        40,
                        false,
                        YHDanmaku.Bullet.BALL.bypass(),
                        RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 2)
                );
                level.addFreshEntity(vicedanmaku);
            }
        }
    }
}
