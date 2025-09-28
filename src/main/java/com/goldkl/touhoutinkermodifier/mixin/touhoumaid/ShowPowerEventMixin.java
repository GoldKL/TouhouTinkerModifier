package com.goldkl.touhoutinkermodifier.mixin.touhoumaid;

import com.github.tartaricacid.touhoulittlemaid.client.event.ShowPowerEvent;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShowPowerEvent.class)
public class ShowPowerEventMixin {
    @WrapOperation(method = "onRenderOverlay",at = @At(value = "INVOKE",
            target = "Lcom/github/tartaricacid/touhoulittlemaid/item/ItemHakureiGohei;isGohei(Lnet/minecraft/world/item/ItemStack;)Z"),
    remap = false)
    private static boolean onRenderOverlay_isGoheimixin(ItemStack stack, Operation<Boolean> original)
    {
        return original.call(stack) || stack.is(TagsRegistry.ItemsTag.GOHEI);
    }
}
