package com.goldkl.touhoutinkermodifier.hook;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

//这个钩子运行在客户端
@OnlyIn(Dist.CLIENT)
public interface NightVisionHook {
    default boolean cannightvision(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,boolean isnightVision)
    {
        return isnightVision;
    }
    default float getnightvisionscale(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,float basescale,float scale)
    {
        return scale;
    }
    record AllMerger(Collection<NightVisionHook> modules) implements NightVisionHook {
        @Override
        public boolean cannightvision(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,boolean isnightVision) {
            for (NightVisionHook module : modules) {
                //只要能夜视就夜视
                isnightVision = module.cannightvision(tool, modifier, context, slotType, isnightVision);
                if (isnightVision) {
                    break;
                }
            }
            return isnightVision;
        }
        @Override
        public float getnightvisionscale(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,float basescale,float scale) {
            for (NightVisionHook module : modules) {
                //标准夜视是1.0f
                scale = module.getnightvisionscale(tool, modifier, context, slotType,basescale,scale);
            }
            return scale;
        }
    }
}
