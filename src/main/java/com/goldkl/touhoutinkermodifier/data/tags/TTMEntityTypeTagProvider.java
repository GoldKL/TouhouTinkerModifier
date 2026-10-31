package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TTMEntityTypeTagProvider extends EntityTypeTagsProvider {
    public TTMEntityTypeTagProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, p_256572_, TouhouTinkerModifier.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(EntitiesRegistry.FireMagicSlate.get())
                .add(EntitiesRegistry.IceMagicSlate.get())
                .add(EntitiesRegistry.LightningMagicSlate.get())
                .add(EntitiesRegistry.NatureMagicSlate.get())
                .add(EntitiesRegistry.GeoMagicSlate.get());
        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .add(EntitiesRegistry.FireMagicSlate.get())
                .add(EntitiesRegistry.IceMagicSlate.get())
                .add(EntitiesRegistry.LightningMagicSlate.get())
                .add(EntitiesRegistry.NatureMagicSlate.get())
                .add(EntitiesRegistry.GeoMagicSlate.get());
        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(EntitiesRegistry.FireMagicSlate.get())
                .add(EntitiesRegistry.IceMagicSlate.get())
                .add(EntitiesRegistry.LightningMagicSlate.get())
                .add(EntitiesRegistry.NatureMagicSlate.get())
                .add(EntitiesRegistry.GeoMagicSlate.get());

    }
    @Override
    @NotNull
    public String getName() {
        return "Touhou Tinker Modifier Entity Type Tags";
    }
}
