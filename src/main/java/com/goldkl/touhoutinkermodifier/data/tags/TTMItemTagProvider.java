package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;

public class TTMItemTagProvider extends ItemTagsProvider {
    public TTMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, TouhouTinkerModifier.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        addToolTags(ItemsRegistry.spear,
                MULTIPART_TOOL,
                DURABILITY,
                HARVEST,
                MELEE_PRIMARY,
                INTERACTABLE_RIGHT,
                PARRY,
                SMALL_TOOLS ,
                BONUS_SLOTS,
                ItemTags.SWORDS,
                TagsRegistry.ItemsTag.SPEAR);

    }
    @Override
    public String getName() {
        return "Touhou Tinkers Modifier's Item Tags";
    }

    @SafeVarargs
    private void addToolTags(ItemLike tool, TagKey<Item>... tags) {
        Item item = tool.asItem();
        for (TagKey<Item> tag : tags) {
            this.tag(tag).add(item);
        }
    }
}
