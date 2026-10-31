package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.item.ModifiableGoheiItem;
import com.goldkl.touhoutinkermodifier.stat.tools.GoheiMaterialStats;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeSpawnEggItem;
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
    public static final ItemObject<Item> kedama = REGISTRY.register("kedama", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> yinangorb = REGISTRY.register("yinangorb", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> spellpaper = REGISTRY.register("spellpaper", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> persuasionneedle = REGISTRY.register("persuasionneedle", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> konpeito = REGISTRY.register("konpeito", () -> new Item(new Item.Properties()));
    public static final ItemObject<Item> eighttrigramsfurnace = REGISTRY.register("eighttrigramsfurnace", () -> new Item(new Item.Properties()));

    public static final ItemObject<ForgeSpawnEggItem> FIRE_MAGIC_SLATE_SPAWN_EGG = REGISTRY.register("fire_magic_slate_spawn_egg", () -> new ForgeSpawnEggItem(EntitiesRegistry.FireMagicSlate, 0x828192, 0xff1e00, new Item.Properties().stacksTo(64)));
    public static final ItemObject<ForgeSpawnEggItem> LIGHTNING_MAGIC_SLATE_SPAWN_EGG = REGISTRY.register("lightning_magic_slate_spawn_egg", () -> new ForgeSpawnEggItem(EntitiesRegistry.LightningMagicSlate, 0x828192, 0xfffd36, new Item.Properties().stacksTo(64)));
    public static final ItemObject<ForgeSpawnEggItem> NATURE_MAGIC_SLATE_SPAWN_EGG = REGISTRY.register("nature_magic_slate_spawn_egg", () -> new ForgeSpawnEggItem(EntitiesRegistry.NatureMagicSlate, 0x828192, 0x00ae04, new Item.Properties().stacksTo(64)));
    public static final ItemObject<ForgeSpawnEggItem> ICE_MAGIC_SLATE_SPAWN_EGG = REGISTRY.register("ice_magic_slate_spawn_egg", () -> new ForgeSpawnEggItem(EntitiesRegistry.IceMagicSlate, 0x828192, 0x00deff, new Item.Properties().stacksTo(64)));
    public static final ItemObject<ForgeSpawnEggItem> GEO_MAGIC_SLATE_SPAWN_EGG = REGISTRY.register("geo_magic_slate_spawn_egg", () -> new ForgeSpawnEggItem(EntitiesRegistry.GeoMagicSlate, 0x828192, 0x865f1c, new Item.Properties().stacksTo(64)));

    //tool part
    public static final ItemObject<ToolPartItem> GoheiCore = REGISTRY.register("gohei_core", () -> new ToolPartItem(TTMItemUtils.ITEM_PROPS, GoheiMaterialStats.ID));
    //part cast
    public static final CastItemObject GoheiCoreCast = REGISTRY.registerCast(ItemsRegistry.GoheiCore, TTMItemUtils.ITEM_PROPS);
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    public static void addCommonTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        tab.accept(kedama);
        tab.accept(yinangorb);
        tab.accept(spellpaper);
        tab.accept(persuasionneedle);
        tab.accept(konpeito);
        tab.accept(eighttrigramsfurnace);
        tab.accept(FIRE_MAGIC_SLATE_SPAWN_EGG);
        tab.accept(LIGHTNING_MAGIC_SLATE_SPAWN_EGG);
        tab.accept(NATURE_MAGIC_SLATE_SPAWN_EGG);
        tab.accept(ICE_MAGIC_SLATE_SPAWN_EGG);
        tab.accept(GEO_MAGIC_SLATE_SPAWN_EGG);
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
