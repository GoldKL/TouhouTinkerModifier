package com.goldkl.touhoutinkermodifier.mixin.forge;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {
    @WrapOperation(method = "isLivingOnLadder",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isLadder(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z"),remap = false)
    private static boolean StateIsLadderMixin(BlockState instance, LevelReader levelReader, BlockPos blockPos, LivingEntity entity, Operation<Boolean> original)
    {
        if(TTMEntityUtils.hasModifier(entity, TTMModifierIds.tsuchigumo))
        {
            if(instance.is(Blocks.COBWEB))
            {
                return true;
            }
            if(!entity.isSuppressingSlidingDownLadder())
            {
                return entity.horizontalCollision || original.call(instance,levelReader,blockPos,entity);
            }
            else
            {
                AABB boundingBox = entity.getBoundingBox();
                boolean flag = false;
                // 获取边界盒覆盖的方块范围
                BlockPos minPos = BlockPos.containing(boundingBox.minX - 0.01, boundingBox.minY- 0.01, boundingBox.minZ- 0.01);
                BlockPos maxPos = BlockPos.containing(boundingBox.maxX + 0.01, boundingBox.maxY+ 0.01, boundingBox.maxZ+ 0.01);
                // 遍历所有方块
                for (int x = minPos.getX(); x <= maxPos.getX() && !flag; x++) {
                    for (int y = minPos.getY(); y <= maxPos.getY() && !flag; y++) {
                        for (int z = minPos.getZ(); z <= maxPos.getZ() && !flag; z++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = entity.level().getBlockState(pos);
                            // 检查方块是否有碰撞箱
                            VoxelShape shape = state.getCollisionShape(entity.level(), pos);
                            if (!shape.isEmpty() && shape.bounds().move(pos).intersects(boundingBox.expandTowards(new Vec3(-0.01,-0.01,-0.01)).expandTowards(new Vec3(0.01,0.01,0.01)))) {
                                flag = true;
                            }
                        }
                    }
                }
                return flag || original.call(instance,levelReader,blockPos,entity);
            }
        }
        return original.call(instance,levelReader,blockPos,entity);
    }
}
