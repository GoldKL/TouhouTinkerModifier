package com.goldkl.touhoutinkermodifier.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

import static slimeknights.tconstruct.common.TinkerTags.Items.MODIFIABLE;

public class TTMItemUtils {
    public static final Item.Properties ITEM_PROPS = new Item.Properties();
    public static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);
    //come from TC
    @Nullable
    public static IToolStackView getToolStackIfModifiable(ItemStack stack) {
        if (!stack.isEmpty() && stack.is(MODIFIABLE)) {
            return ToolStack.from(stack);
        }
        return null;
    }
    public static boolean isModifiable(ItemStack stack) {
        return !stack.isEmpty() && stack.is(MODIFIABLE);
    }
}
