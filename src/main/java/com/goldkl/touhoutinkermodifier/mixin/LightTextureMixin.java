package com.goldkl.touhoutinkermodifier.mixin;

import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@OnlyIn(Dist.CLIENT)
@Mixin(LightTexture.class)
public class LightTextureMixin {
    @WrapOperation(method = "updateLightTexture"
            ,at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z",ordinal = 0))
    boolean TCNightVision(LocalPlayer instance, MobEffect effect, Operation<Boolean> original)
    {
        boolean flag = original.call(instance, effect);
        if(!flag)
        {
            EquipmentContext context = new EquipmentContext(instance);
            for (EquipmentSlot slotType : EquipmentSlot.values()) {
                IToolStackView toolStack = context.getToolInSlot(slotType);
                if (toolStack != null && !toolStack.isBroken()) {
                    for (ModifierEntry entry : toolStack.getModifierList()) {
                        flag = entry.getHook(ModifierHooksRegistry.NIGHT_VISION_HOOK).cannightvision(toolStack, entry, context, slotType, flag);
                        if (flag) {
                            return flag;
                        }
                    }
                }
            }
        }
        return flag;
    }
    @WrapOperation(method = "updateLightTexture"
            ,at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F",ordinal = 0))
    float getTCNightVisionScale(LivingEntity entity, float p_109110_, Operation<Float> original)
    {
        float base = entity.hasEffect(MobEffects.NIGHT_VISION)? original.call(entity,p_109110_):0.0f;
        float scale = base;
        EquipmentContext context = new EquipmentContext(entity);
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    scale = entry.getHook(ModifierHooksRegistry.NIGHT_VISION_HOOK).getnightvisionscale(toolStack, entry, context, slotType,base, scale);
                }
            }
        }
        return scale;
    }
}
