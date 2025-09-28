package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.item.ModifiableGoheiItem;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemsRegistry {
    private static final ItemDeferredRegisterExtension REGISTRY = new ItemDeferredRegisterExtension(TouhouTinkerModifier.MODID);

    public static final ItemObject<ModifiableItem> Spear = REGISTRY.register("spear", () -> new ModifiableSwordItem(TTMItemUtils.UNSTACKABLE_PROPS, TTMToolDefinitions.SPEAR));
    public static final ItemObject<ModifiableItem> BrassKnuckles = REGISTRY.register("brass_knuckles", () -> new ModifiableSwordItem(new Item.Properties().durability(-1).stacksTo(2), TTMToolDefinitions.BRASS_KNUCKLES, 2));
    public static final ItemObject<ModifiableItem> Gohei = REGISTRY.register("gohei", () -> new ModifiableGoheiItem(TTMItemUtils.UNSTACKABLE_PROPS, TTMToolDefinitions.GOHEI));
    //material
    public static final ItemObject<Item> yinangorb = REGISTRY.register("yinangorb", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> spellpaper = REGISTRY.register("spellpaper", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> persuasionneedle = REGISTRY.register("persuasionneedle", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> konpeito = REGISTRY.register("konpeito", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> eighttrigramsfurnace = REGISTRY.register("eighttrigramsfurnace", () -> new Item(new Item.Properties()));

    //tool part
    public static final ItemObject<ToolPartItem> GoheiCore = REGISTRY.register("gohei_core", () -> new ToolPartItem(TTMItemUtils.ITEM_PROPS, GoheiMaterialStats.ID));
    //part cast
    public static final CastItemObject GoheiCoreCast = REGISTRY.registerCast(ItemsRegistry.GoheiCore, TTMItemUtils.ITEM_PROPS);
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    public static void addCommonTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        tab.accept(yinangorb);
        tab.accept(spellpaper);
        tab.accept(persuasionneedle);
        tab.accept(konpeito);
        tab.accept(eighttrigramsfurnace);
    }
    public static void addPartTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Objects.requireNonNull(tab);
        Consumer<ItemStack> output = tab::accept;
        accept(output, GoheiCore);
        addCasts(tab, CastItemObject::get);
        addCasts(tab, CastItemObject::getSand);
        addCasts(tab, CastItemObject::getRedSand);

    }
    public static void addToolTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Consumer<ItemStack> output = tab::accept;
        acceptTool(output, Spear);
        acceptTool(output, BrassKnuckles);
        acceptTool(output, Gohei);
    }
    private static void addCasts(CreativeModeTab.Output output, Function<CastItemObject,ItemLike> getter)
    {
        accept(output, getter, GoheiCoreCast);
    }
    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
        ToolBuildHandler.addVariants(output, (IModifiable)tool.get(), "");
    }
    private static void acceptTools(Consumer<ItemStack> output, EnumObject<?, ? extends IModifiable> tools) {
        tools.forEach((tool) -> ToolBuildHandler.addVariants(output, tool, ""));
    }
    private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
        item.get().addVariants(output, "");
    }
    private static void accept(CreativeModeTab.Output output, Function<CastItemObject,ItemLike> getter, CastItemObject cast) {
        output.accept(getter.apply(cast));
    }
}
