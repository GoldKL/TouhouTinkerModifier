package com.goldkl.touhoutinkermodifier.mixin.arsnouveau;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellResolver.class)
public class SpellResolverMixin {
    @Shadow(remap = false)
    public SpellContext spellContext;
    @WrapOperation(method = "getResolveCost",at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I"),remap = false)
    int getResolveCostmixin(int a, int b, Operation<Integer> original)
    {
        LivingEntity entity = spellContext.getUnwrappedCaster();
        double entityManaCostReduction = entity.getAttributeValue(AttributesRegistry.MANA_COST_REDUCTION.get());
        return original.call(a, (int)(b * (2 - Utils.softCapFormula(entityManaCostReduction))));
    }
}
