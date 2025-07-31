package com.goldkl.touhoutinkermodifier.mixin.sakuratinker;

import com.goldkl.touhoutinkermodifier.modifiers.sakuratinker.TTMFountainMagicModifier;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.FountainMagicModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import java.util.function.Supplier;

@Mixin(ISSCompat.class)
public class ISSCompatMixin {
    @Unique
    private static int touhouTinkerModifier$index = 0;
    @Shadow
    public static StaticModifier<TTMFountainMagicModifier> FountainMagic;
    @Shadow
    public static ModifierDeferredRegister ISS_MODIFIERS;
    @WrapOperation(method = "<clinit>",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/modifiers/util/ModifierDeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lslimeknights/tconstruct/library/modifiers/util/StaticModifier;"))
    private static <T extends Modifier> StaticModifier<T> Registermixin(ModifierDeferredRegister instance, String name, Supplier<? extends T> supplier, Operation<StaticModifier<T>> original)
    {
        if(name.equals("fountain_magic")&&touhouTinkerModifier$index == 0)
        {
            touhouTinkerModifier$index ++;
            return null;
        }
        return original.call(instance, name, supplier);
    }
    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void clinitmixin(CallbackInfo ci)
    {
        FountainMagic = ISS_MODIFIERS.register("fountain_magic", TTMFountainMagicModifier::new);
    }

}
