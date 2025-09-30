package com.goldkl.touhoutinkermodifier.bullettype.marisa;

import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import com.goldkl.touhoutinkermodifier.entity.danmaku.ModifiableDamakuEntity;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractMarisaBulletType extends AbstractBulletType {

    @Override
    public double getHitboxFix() {
        return 0.1;
    }

    @Override
    public boolean isShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        return remainingTicks % 20 == 0;
    }

    public void MarisaCommonShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks)
    {
        if(!level.isClientSide)
        {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            int power = TTMEntityUtils.getPowerValue(entity);
            int kpower = Math.min(power + 1, 5);
            for(int i = 0; i < kpower; i++)
            {
                float midpoint = kpower/2.0f;
                float angle = 20f;
                ModifiableDamakuEntity danmaku = new ModifiableDamakuEntity(EntitiesRegistry.ModifiableDanmaku.get(), entity, level);
                danmaku.setTool(itemStack, entity.getUsedItemHand());
                danmaku.setItem(YHDanmaku.Bullet.SPARK.get(DyeColor.BLUE).asStack());
                danmaku.setPos(danmaku.position()
                        .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (i - midpoint + 0.5f) * angle, 2)));
                danmaku.setup(
                        YHDanmaku.Bullet.SPARK.damage(),
                        40,
                        false,
                        YHDanmaku.Bullet.SPARK.bypass(),
                        RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 2)
                );
                level.addFreshEntity(danmaku);
            }
        }
    }
}
