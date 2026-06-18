package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.helper.SpellDamageSourceInterface;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(SpellDamageSource.class)
public abstract class SpellDamageSourceMixin extends DamageSource implements SpellDamageSourceInterface {
    @Shadow(remap = false)
    float lifesteal;
    @Unique
    float touhouTinkerModifier$passresist = 0;
    public SpellDamageSourceMixin(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }
    @Inject(method = "getLifestealPercent",at = @At("HEAD"), cancellable = true,remap = false)
    public void getLifestealPercent(CallbackInfoReturnable<Float> cir) {
        boolean flag = false;
        if(this.getEntity() instanceof LivingEntity entity)
        {
            for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
            {
                IToolStackView tool = TTMItemUtils.getToolStackIfModifiable(entity.getItemBySlot(equipmentSlot));
                if(tool != null && tool.getModifier(TTMModifierIds.scarletdevil)!= ModifierEntry.EMPTY)
                {
                    flag = true;
                    break;
                }
            }
        }
        if(flag)
        {
            cir.setReturnValue(2 * lifesteal);
        }
    }
    @Unique
    public SpellDamageSource touhouTinkerModifier$setPassbyResist(float p){
        touhouTinkerModifier$passresist = Mth.clamp(p,0,1);
        return (SpellDamageSource)(Object)this;
    }
    @Unique
    public float touhouTinkerModifier$getPassByResist(){
        return touhouTinkerModifier$passresist;
    }
}
