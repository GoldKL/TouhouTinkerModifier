package com.goldkl.touhoutinkermodifier.bullettype.reimu;

import com.goldkl.touhoutinkermodifier.entity.danmaku.ModifiableDamakuEntity;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ReimuBBulletType extends AbstractReimuBulletType {
    public String getTypeName()
    {
        return "reimu_type_b_bullet";
    }

    @Override
    public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if(!level.isClientSide)
        {
            int power = TTMEntityUtils.getPowerValue(entity);
            this.ReimuCommonShootBulletTick(level, entity, itemStack, remainingTicks);
            if(power > 0)
            {
                int k = (power + 1) / 2;
                for(int i = 0; i < 2; i++)
                {
                    for(int j = 0; j < k; j++)
                    {
                        float angle = 180f;
                        ModifiableDamakuEntity danmaku = new ModifiableDamakuEntity(EntitiesRegistry.ModifiableDanmaku.get(), entity, level);
                        danmaku.setTool(itemStack, entity.getUsedItemHand());
                        danmaku.setItem(YHDanmaku.Bullet.BALL.get(DyeColor.GRAY).asStack());
                        danmaku.setPos(danmaku.position()
                                .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (i - 0.5f) * angle, 2))
                                .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (j - k/2.0f) * 30, 1.5)));
                        danmaku.setup(
                                YHDanmaku.Bullet.BALL.damage() + 1,
                                40,
                                false,
                                YHDanmaku.Bullet.BALL.bypass(),
                                RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 3)
                        );
                        level.addFreshEntity(danmaku);
                    }
                }
            }
        }
    }
}
