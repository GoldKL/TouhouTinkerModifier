package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.material.TTMMaterialIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class TTMMaterialTagProvider extends AbstractMaterialTagProvider {
    public TTMMaterialTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TouhouTinkerModifier.MODID, existingFileHelper);
    }
    @Override
    protected void addTags() {
        tag(TagsRegistry.MaterialsTag.Satori);
        tag(TagsRegistry.MaterialsTag.Koishi);
        tag(TagsRegistry.MaterialsTag.Maribel);
        tag(TagsRegistry.MaterialsTag.Renko);
        tag(TagsRegistry.MaterialsTag.IzayoiSakuya);
        tag(TagsRegistry.MaterialsTag.Seijakijin);
        tag(TagsRegistry.MaterialsTag.Seijakijin_Tier0)
                .add(TTMMaterialIds.kedama)
                .add(MaterialIds.string)
                .add(MaterialIds.leather);
    }
    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Material Tag Provider";
    }
}