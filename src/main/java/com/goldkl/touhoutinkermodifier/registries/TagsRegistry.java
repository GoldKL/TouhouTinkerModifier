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
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

import static slimeknights.tconstruct.TConstruct.getResource;

public class TagsRegistry {
    public static class ItemsTag{
        public static final TagKey<Item> SPEAR = local("modifiable/melee/spear");
        public static final TagKey<Item> CLAYMORE = local("modifiable/melee/claymore");
        public static final TagKey<Item> DAGGER = local("modifiable/melee/dagger");
        public static final TagKey<Item> GOHEI = local("modifiable/gohei");
        public static final TagKey<Item> TECHNOLOGY = local("modifiable/technology");

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
    public static class MaterialsTag {
        public static final TagKey<IMaterial> Koishi = local("koishi");
        public static final TagKey<IMaterial> Satori = local("satori");
        public static final TagKey<IMaterial> Maribel = local("maribel");
        public static final TagKey<IMaterial> Renko = local("renko");
        public static final TagKey<IMaterial> IzayoiSakuya = local("izayoisakuya");
        public static final TagKey<IMaterial> Seijakijin= local("seijakijin");
        public static final TagKey<IMaterial> Seijakijin_Tier0= local("seijakijin_tier0");

        private static TagKey<IMaterial> local(String name) {
            return TagKey.create(MaterialManager.REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, name));
        }
    }
}
