package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;


public class TTMModifierTagProvider extends AbstractModifierTagProvider {
    public TTMModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TouhouTinkerModifier.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TagsRegistry.ModifiersTag.ScarletDevilMansion)//红魔馆
                    .add(ModifierIds.scarletdevil)//蕾米&芙兰
                    .add(ModifierIds.gungnir,ModifierIds.youngscarletmoon,ModifierIds.crimsonfantasy)//蕾米
                    .add(ModifierIds.laevatein,ModifierIds.underredmoon,ModifierIds.andthentherewerenone)//芙兰
                    .add(ModifierIds.devillibrarian)//姆Q&小恶魔
                    .add(ModifierIds.everbrightscarlettower,ModifierIds.redblacktree)//小恶魔
                    .add(ModifierIds.overpowermq,ModifierIds.sevenluminarieswizard)//姆Q
                    .add(ModifierIds.killerdoll,ModifierIds.vanhelsingprogeny,ModifierIds.jacktheripper,ModifierIds.perfectandelegant,ModifierIds.pocketwatchofblood,ModifierIds.theworld)//十六夜咲夜
                    .add(ModifierIds.rainbowtaichi,ModifierIds.fanghuaxuanlan,ModifierIds.bengshancaiji);//红美铃
        tag(TinkerTags.Modifiers.OVERSLIME_FRIEND)//黏液之友
                    .add(ModifierIds.overpowermq)
                    .add(ModifierIds.overrose)
                    .add(ModifierIds.overthinking);
        tag(TagsRegistry.ModifiersTag.SilverModifier)//银质
                    .add(ModifierIds.jacktheripper);
        tag(TinkerTags.Modifiers.PROTECTION_DEFENSE)//保护
                    .add(ModifierIds.thebloodofoni);
        tag(TagsRegistry.ModifiersTag.Chireiden)//地灵殿
                .add(ModifierIds.overrose, ModifierIds.missmary, ModifierIds.koishiseye, ModifierIds.loveburiedembers, ModifierIds.idliberation, ModifierIds.superegoinhibition)//恋恋
                .add(ModifierIds.tsuchigumo, ModifierIds.fieldmiasma)//山女
                .add(ModifierIds.welldestructor)//小桶
                .add(ModifierIds.triplefatality, ModifierIds.thebloodofoni, ModifierIds.thepowerofmountain)//勇仪
                .add(ModifierIds.ushinokokumairi,ModifierIds.ushinokokumairishichi,ModifierIds.greeneyes)//帕露西
                .add(ModifierIds.tokamak, ModifierIds.undergroundsun, ModifierIds.thousandtrillionflare, ModifierIds.hellcrow)//阿空
                .add(ModifierIds.terriblesouvenir, ModifierIds.overthinking)//觉
                .add(ModifierIds.hellstrafficaccident, ModifierIds.rekindlingofdeadashes);//猫燐
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier Tag Provider";
    }
}
