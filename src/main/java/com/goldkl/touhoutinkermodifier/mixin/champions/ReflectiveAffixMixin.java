package com.goldkl.touhoutinkermodifier.mixin.champions;

import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.affix.ReflectiveAffix;

@Mixin(ReflectiveAffix.class)
public class ReflectiveAffixMixin {
    @Inject(method = "onDamage", at = @At("HEAD"),remap = false, cancellable = true)
    void onDamageMixin(IChampion champion, DamageSource source, float amount, float newAmount, CallbackInfoReturnable<Float> cir){
        if(source.is(TagsRegistry.DamageTypeTag.PASS_REFLECTIVE))
        {
            cir.setReturnValue(newAmount);
        }
    }
}
