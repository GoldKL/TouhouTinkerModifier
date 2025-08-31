package com.goldkl.touhoutinkermodifier.mixin.lootr;

import com.goldkl.touhoutinkermodifier.modifiers.KomeijisistersModifier;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import noobanidus.mods.lootr.api.LootFiller;
import noobanidus.mods.lootr.data.ChestData;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@Mixin(ChestData.class)
public class ChestDataMixin {
    @Inject(method = "createInventory(Lnet/minecraft/server/level/ServerPlayer;Lnoobanidus/mods/lootr/api/LootFiller;Ljava/util/function/IntSupplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/LongSupplier;)Lnoobanidus/mods/lootr/data/SpecialChestInventory;",at = @At("RETURN"),remap = false)
    void createInventoryMixin(ServerPlayer player, LootFiller filler, IntSupplier sizeSupplier, Supplier<Component> displaySupplier, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier, CallbackInfoReturnable<SpecialChestInventory> cir)
    {
        touhouTinkerModifier$isdisableKoishi(player,cir);
    }
    @Inject(method = "createInventory(Lnet/minecraft/server/level/ServerPlayer;Lnoobanidus/mods/lootr/api/LootFiller;Lnet/minecraft/world/level/block/entity/BaseContainerBlockEntity;Ljava/util/function/Supplier;Ljava/util/function/LongSupplier;)Lnoobanidus/mods/lootr/data/SpecialChestInventory;",at = @At("RETURN"),remap = false)
    void createInventoryMixin(ServerPlayer player, LootFiller filler, BaseContainerBlockEntity blockEntity, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier, CallbackInfoReturnable<SpecialChestInventory> cir)
    {
        touhouTinkerModifier$isdisableKoishi(player,cir);
    }
    @Inject(method = "createInventory(Lnet/minecraft/server/level/ServerPlayer;Lnoobanidus/mods/lootr/api/LootFiller;Lnet/minecraft/world/level/block/entity/RandomizableContainerBlockEntity;)Lnoobanidus/mods/lootr/data/SpecialChestInventory;",at = @At("RETURN"),remap = false)
    void createInventoryMixin(ServerPlayer player, LootFiller filler, RandomizableContainerBlockEntity tile, CallbackInfoReturnable<SpecialChestInventory> cir)
    {
        touhouTinkerModifier$isdisableKoishi(player,cir);
    }
    @Unique
    void touhouTinkerModifier$isdisableKoishi(ServerPlayer player, CallbackInfoReturnable<SpecialChestInventory> cir)
    {
        if(cir.getReturnValue() != null)
        {
            if (player != null && !TTMEntityUtils.HasMatchModifierTool(player, KomeijisistersModifier::KomeijisisterscanUse)) {
                EffectEventHandlers.disableKoishi(player);
            }
        }
    }
}
