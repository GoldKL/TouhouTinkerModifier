package com.goldkl.touhoutinkermodifier.bullettype.reimu;

import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import com.goldkl.touhoutinkermodifier.entity.danmaku.TrackDanmakuEntity;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ReimuABulletType extends AbstractReimuBulletType {
    public String getTypeName()
    {
        return "reimu_type_a_bullet";
    }

    @Override
    public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if(!level.isClientSide)
        {
            int power = TTMEntityUtils.getPowerValue(entity);
            this.ReimuCommonShootBulletTick(level, entity, itemStack, remainingTicks);
            if(power > 0 && remainingTicks % (20 *(6 - power)) == 0)
            {
                var raycast = Utils.raycastForEntity(level, entity, 32, true);
                for(int i = 0; i < 2; i++)
                {
                    float angle = 180f;
                    TrackDanmakuEntity danmaku = new TrackDanmakuEntity(EntitiesRegistry.TrackDanmaku.get(), entity, level);
                    danmaku.setTool(itemStack, entity.getUsedItemHand());
                    danmaku.setItem(YHDanmaku.Bullet.BALL.get(DyeColor.GRAY).asStack());
                    danmaku.setPos(danmaku.position()
                            .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (i - 0.5f) * angle, 2)));
                    danmaku.setup(
                            YHDanmaku.Bullet.BALL.damage() - 1,
                            60,
                            false,
                            YHDanmaku.Bullet.BALL.bypass(),
                            RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 2)
                    );
                    if(raycast instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget)
                    {
                        danmaku.setTarget(livingTarget);
                    }
                    level.addFreshEntity(danmaku);
                }
            }
        }
    }
}
