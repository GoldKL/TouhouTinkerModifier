package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.utils.TTMItemUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class ItemsRegistry {
    private static final ItemDeferredRegisterExtension REGISTRY = new ItemDeferredRegisterExtension(TouhouTinkerModifier.MODID);

    public static final ItemObject<ModifiableItem> spear = REGISTRY.register("spear", () -> new ModifiableSwordItem(TTMItemUtils.UNSTACKABLE_PROPS, TTMToolDefinitions.SPEAR));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
