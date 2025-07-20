package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

public class TouhouModifierTagProvider extends AbstractModifierTagProvider {
    public TouhouModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TouhouTinkerModifier.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TagsRegistry.ModifiersTag.ScarletDevilMansion)
                .add(ModifierIds.scarletdevil)//蕾米&芙兰
                .add(ModifierIds.gungnir,ModifierIds.youngscarletmoon,ModifierIds.crimsonfantasy)//蕾米
                .add(ModifierIds.laevatein,ModifierIds.underredmoon,ModifierIds.andthentherewerenone)//芙兰
                .add(ModifierIds.devillibrarian)//姆Q&小恶魔
                .add(ModifierIds.everbrightscarlettower,ModifierIds.redblacktree)//小恶魔
                .add(ModifierIds.overpowermq,ModifierIds.sevenluminarieswizard)//姆Q
                .add(ModifierIds.killerdoll,ModifierIds.vanhelsingprogeny,ModifierIds.jacktheripper,ModifierIds.perfectandelegant,ModifierIds.pocketwatchofblood,ModifierIds.theworld)//十六夜咲夜
                .add(ModifierIds.rainbowtaichi,ModifierIds.fanghuaxuanlan,ModifierIds.bengshancaiji);//红美铃
        tag(TinkerTags.Modifiers.OVERSLIME_FRIEND)
                .add(ModifierIds.overpowermq);
        tag(TagsRegistry.ModifiersTag.SilverModifier)
                .add(ModifierIds.jacktheripper);
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier Tag Provider";
    }
}
