package com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshSaucerItem;
import dev.xkmc.youkaishomecoming.content.item.food.IFleshFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.Objects;

@Mixin(FleshSaucerItem.class)
public abstract class FleshSaucerItemMixin implements IFleshFoodItem {
    @Inject(method = "getName",at = @At("RETURN"),cancellable = true)
    void getNameMixin(ItemStack pStack, CallbackInfoReturnable<Component> cir)
    {
        Player player = IFleshFoodItem.getPlayer();
        if(player != null)
        {
            if(TTMEntityUtils.hasModifiers(player, TTMEntityUtils.FleshModifiers))
            {
                Component name = YHLangData.FLESH_NAME_YOUKAI.get();
                cir.setReturnValue(Component.translatable(this.asItem().getDescriptionId(pStack), name));
            }
        }
    }
    @WrapOperation(method = "appendHoverText",at =  @At(value = "INVOKE", target = "Ldev/xkmc/youkaishomecoming/content/item/food/FleshSaucerItem;appendFleshText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"))
    public void appendHoverTextMixin(FleshSaucerItem instance, ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag, Operation<Void> original)
    {
        Player player = IFleshFoodItem.getPlayer();
        if(player != null && TTMEntityUtils.hasModifiers(player, TTMEntityUtils.FleshModifiers))
        {
            list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
            if (this.asItem() == YHFood.FLESH.item.get()) {
                Component obt;
                MutableComponent fying = Component.translatable((YHEffects.YOUKAIFYING.get()).getDescriptionId());
                MutableComponent fied = Component.translatable((YHEffects.YOUKAIFIED.get()).getDescriptionId());
                obt = YHLangData.OBTAIN_FLESH.get(fying, fied);
                list.add(YHLangData.OBTAIN.get(new Object[0]).append(obt));
            }
            return;
        }
        original.call(instance, itemStack, level , list, tooltipFlag);
    }
    @Inject(method = "getFoodProperties",at = @At("RETURN"),cancellable = true,remap = false)
    public void getFoodPropertiesMixin(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<FoodProperties> cir)
    {
        FoodProperties old = cir.getReturnValue();
        if(old != null)
        {
            FoodProperties.Builder builder = new FoodProperties.Builder();
            boolean flag = false;
            if(entity != null)
            {
                if(TTMEntityUtils.hasModifier(entity,ModifierIds.devourdarkness))
                {
                    builder.nutrition(20);
                    builder.saturationMod(1f);
                    builder.alwaysEat();
                    flag = true;
                }
                else if(TTMEntityUtils.hasModifier(entity,ModifierIds.apparitionsstalkthenight))
                {
                    builder.nutrition((int) (old.getNutrition() * 1.5));
                    builder.saturationMod(old.getSaturationModifier());
                    flag = true;
                }
            }
            if(flag)
            {
                if (old.canAlwaysEat()) {
                    builder.alwaysEat();
                }

                if (old.isFastFood()) {
                    builder.fast();
                }

                if (old.isMeat()) {
                    builder.meat();
                }

                for(Pair<MobEffectInstance, Float> ent : old.getEffects()) {
                    if (!((MobEffectInstance)ent.getFirst()).getEffect().isBeneficial()) {
                        Objects.requireNonNull(ent);
                        builder.effect(ent::getFirst, (Float)ent.getSecond());
                    }
                }
                cir.setReturnValue(builder.build());
            }
        }
    }
}
