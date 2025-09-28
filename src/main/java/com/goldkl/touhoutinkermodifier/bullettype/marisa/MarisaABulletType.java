package com.goldkl.touhoutinkermodifier.bullettype.marisa;

import com.goldkl.touhoutinkermodifier.entity.danmaku.ModifiableDamakuEntity;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MarisaABulletType extends AbstractMarisaBulletType {
    public String getTypeName()
    {
        return "marisa_type_a_bullet";
    }

    @Override
    public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if(!level.isClientSide)
        {
            int power = TTMEntityUtils.getPowerValue(entity);
            this.MarisaCommonShootBulletTick(level, entity, itemStack, remainingTicks);
            if(power > 0 && remainingTicks % (40 *(6 - power)) == 0)
            {
                boolean flag = entity.isCrouching();
                float angle = flag?0:180;
                for(int i = 0; i < (flag?1:2); i++)
                {
                    ModifiableDamakuEntity danmaku = new ModifiableDamakuEntity(EntitiesRegistry.ModifiableDanmaku.get(), entity, level);
                    danmaku.setTool(itemStack, entity.getUsedItemHand());
                    danmaku.setItem(YHDanmaku.Bullet.STAR.get(flag?DyeColor.BLUE:DyeColor.GREEN).asStack());
                    danmaku.setPos(danmaku.position()
                            .add(RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot() + (i - 0.5f) * angle, 2)));
                    danmaku.setup(
                            YHDanmaku.Bullet.STAR.damage() + (flag?2:-2),
                            40,
                            false,
                            YHDanmaku.Bullet.STAR.bypass(),
                            RayTraceUtil.getRayTerm(Vec3.ZERO, entity.getXRot(), entity.getYRot(), 2)
                    );
                    level.addFreshEntity(danmaku);
                }
            }
        }
    }
}
