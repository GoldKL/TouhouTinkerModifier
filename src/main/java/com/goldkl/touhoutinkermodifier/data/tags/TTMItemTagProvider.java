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
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;

public class TTMItemTagProvider extends ItemTagsProvider {
    public TTMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, TouhouTinkerModifier.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        IntrinsicTagAppender<Item> goldCasts = this.tag(TinkerTags.Items.GOLD_CASTS);
        IntrinsicTagAppender<Item> sandCasts = this.tag(TinkerTags.Items.SAND_CASTS);
        IntrinsicTagAppender<Item> redSandCasts = this.tag(TinkerTags.Items.RED_SAND_CASTS);
        IntrinsicTagAppender<Item> singleUseCasts = this.tag(TinkerTags.Items.SINGLE_USE_CASTS);
        IntrinsicTagAppender<Item> multiUseCasts = this.tag(TinkerTags.Items.MULTI_USE_CASTS);
        Consumer<CastItemObject> addCast = cast -> {
            // tag based on material
            goldCasts.add(cast.get());
            sandCasts.add(cast.getSand());
            redSandCasts.add(cast.getRedSand());
            // tag based on usage
            singleUseCasts.addTag(cast.getSingleUseTag());
            this.tag(cast.getSingleUseTag()).add(cast.getSand(), cast.getRedSand());
            multiUseCasts.addTag(cast.getMultiUseTag());
            this.tag(cast.getMultiUseTag()).add(cast.get());
        };
        addToolTags(ItemsRegistry.Spear,
                MULTIPART_TOOL,
                DURABILITY,
                HARVEST,
                MELEE_PRIMARY,
                INTERACTABLE_RIGHT,
                SMALL_TOOLS ,
                BONUS_SLOTS,
                ItemTags.SWORDS,
                TagsRegistry.ItemsTag.SPEAR);

        addToolTags(ItemsRegistry.BrassKnuckles,
                MULTIPART_TOOL,
                DURABILITY,
                HARVEST,
                MELEE_PRIMARY,
                HELD_ARMOR,
                INTERACTABLE_RIGHT,
                SMALL_TOOLS ,
                BONUS_SLOTS,
                UNSALVAGABLE);

        addToolTags(ItemsRegistry.Gohei,
                MULTIPART_TOOL,
                DURABILITY,
                HARVEST,
                TagsRegistry.ItemsTag.GOHEI,
                MELEE_PRIMARY,
                INTERACTABLE_RIGHT,
                SMALL_TOOLS ,
                BONUS_SLOTS,
                ItemTags.SWORDS);

        tag(TagsRegistry.ItemsTag.DAGGER)
                .add(TinkerTools.dagger.get());

        tag(BARTERED_PARTS)
                .add(ItemsRegistry.GoheiCore.get());
        addCast.accept(ItemsRegistry.GoheiCoreCast);

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
