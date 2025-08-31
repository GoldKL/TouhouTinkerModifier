package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.DamageTypesRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


public class TTMDamageTypesTagProvider extends DamageTypeTagsProvider {
    public TTMDamageTypesTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookup, TouhouTinkerModifier.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER)
                .add(DamageTypes.GENERIC_KILL)
                .add(DamageTypes.FELL_OUT_OF_WORLD)
                .add(DamageTypes.OUTSIDE_BORDER);
        tag(TagsRegistry.DamageTypeTag.PASS_WORLD_ENDER)
                .addTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .addOptional(ISSDamageTypes.HEARTSTOP.location());
        DamageTypeAddTag(DamageTypesRegistry.radiation_hurt,
                DamageTypeTags.BYPASSES_ARMOR,
                DamageTypeTags.BYPASSES_COOLDOWN,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS,
                DamageTypeTags.BYPASSES_RESISTANCE);
    }
    @SafeVarargs
    private void DamageTypeAddTag(ResourceKey<DamageType> damagetype, TagKey<DamageType>...tagKeys)
    {
        for(TagKey<DamageType> tagKey : tagKeys)
        {
            tag(tagKey).add(damagetype);
        }
    }
    @Override
    public String getName() {
        return "Touhou Tinkers Modifier's Damage Type Tags";
    }
}
