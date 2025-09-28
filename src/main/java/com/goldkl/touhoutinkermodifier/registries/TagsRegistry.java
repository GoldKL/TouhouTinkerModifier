package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

public class TagsRegistry {
    public static class ItemsTag{
        public static final TagKey<Item> SPEAR = local("modifiable/melee/spear");
        public static final TagKey<Item> CLAYMORE = local("modifiable/melee/claymore");
        public static final TagKey<Item> DAGGER = local("modifiable/melee/dagger");
        public static final TagKey<Item> GOHEI = local("modifiable/gohei");

        /** Makes a tag in the touhoutinkermodifier domain **/
        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name));
        }

        /** Makes a tag in the forge domain **/
        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", name));
        }
    }
    public static class DamageTypeTag{
        public static final TagKey<DamageType> PASS_REFLECTIVE = local("pass_reflective");
        public static final TagKey<DamageType> PASS_PORTION_MODIFIER = local("pass_portion_modifier");
        public static final TagKey<DamageType> PASS_WORLD_ENDER = local("pass_world_ender");
        private static TagKey<DamageType> local(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name));
        }

        private static TagKey<DamageType> common(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("forge", name));
        }
    }
    public static class ModifiersTag{
        public static final TagKey<Modifier> ScarletDevilMansion = local("scarlet_devil_mansion");
        public static final TagKey<Modifier> SilverModifier = local("silver_modifier");
        public static final TagKey<Modifier> Chireiden = local("chireiden");
        private static TagKey<Modifier> local(String name) {
            return TagKey.create(ModifierManager.REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name));
        }
    }
}
