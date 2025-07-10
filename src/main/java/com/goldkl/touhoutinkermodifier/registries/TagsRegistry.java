package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public class TagsRegistry {
    public static class ItemsTag{
        public static final TagKey<Item> SPEAR = local("modifiable/melee/spear");
        public static final TagKey<Item> CLAYMORE = local("modifiable/melee/claymore");

        /** Makes a tag in the touhoutinkermodifier domain **/
        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name));
        }

        /** Makes a tag in the forge domain **/
        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", name));
        }
    }
}
