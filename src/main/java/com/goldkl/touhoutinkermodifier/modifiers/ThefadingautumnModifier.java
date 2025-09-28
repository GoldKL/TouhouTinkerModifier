package com.goldkl.touhoutinkermodifier.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.armor.ArmorWalkRadiusModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import vectorwing.farmersdelight.common.block.WildCropBlock;
import vectorwing.farmersdelight.common.block.WildRiceBlock;

public class ThefadingautumnModifier extends Modifier implements ArmorWalkRadiusModule<Void> {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(this);
    }

    @Override
    public float getRadius(IToolStackView tool, ModifierEntry modifier) {
        return (2.5f + tool.getModifierLevel(TinkerModifiers.expanded.getId())) * modifier.getLevel();
    }

    @Override
    public boolean walkOn(IToolStackView tool, ModifierEntry entry, LivingEntity living, Level world, BlockPos target, BlockPos.MutableBlockPos mutable, Void context) {
        BlockState blockState = world.getBlockState(target);
        if (checkblock(blockState)) {
            world.destroyBlock(target, true, living);
        }
        return false;
    }
    private boolean checkblock(BlockState blockState) {
        Block block = blockState.getBlock();
        if(block instanceof LeavesBlock && !blockState.getValue(LeavesBlock.PERSISTENT)) {
            //树叶，手动放置的无视
            return true;
        }
        if(block instanceof WildCropBlock|| block instanceof WildRiceBlock) {
            //农夫乐事兼容：野生作物
            return true;
        }
        if(block == Blocks.LARGE_FERN || block == Blocks.TALL_GRASS) {
            //高草和高蕨
            return true;
        }
        if(block == Blocks.GRASS ||
                block == Blocks.FERN ||
                block == Blocks.DEAD_BUSH ||
                block == Blocks.SEAGRASS ||
                block == Blocks.TALL_SEAGRASS) {
            //草，蕨，枯萎灌木，海草，高海草
            return true;
        }
        if(block == Blocks.BROWN_MUSHROOM_BLOCK ||
                block == Blocks.RED_MUSHROOM_BLOCK ||
                block == Blocks.MUSHROOM_STEM) {
            //蘑菇块，蘑菇梗
            return true;
        }
        if(block instanceof TallFlowerBlock || block instanceof FlowerBlock) {
            //花与高花
            return true;
        }
        return false;
    }
    @Override
    public void onWalk(IToolStackView tool, ModifierEntry modifier, LivingEntity living, BlockPos prevPos, BlockPos newPos)
    {
        Level world = living.level();
        if (living.onGround() && !tool.isBroken() && !world.isClientSide) {
            Void context = this.getContext(tool, modifier, living, prevPos, newPos);
            float trueRadius = Math.min(16.0F, this.getRadius(tool, modifier));
            int radius = Mth.floor(trueRadius);
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            Vec3 posVec = living.position();
            BlockPos center = BlockPos.containing(posVec.x, posVec.y + 0.5, posVec.z);
            ToolDamageUtil.damageAnimated(tool, 1, living, EquipmentSlot.FEET);
            for(BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, 0, -radius), center.offset(radius, 3, radius))) {
                if (pos.closerToCenterThan(new Vec3(living.position().x, pos.getY(), living.position().z) , trueRadius) && this.walkOn(tool, modifier, living, world, pos, mutable, context)) {
                    break;
                }
            }
        }
    }
}
