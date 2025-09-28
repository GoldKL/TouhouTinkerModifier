package com.goldkl.touhoutinkermodifier.data.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
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
                    .add(TTMModifierIds.scarletdevil)//蕾米&芙兰
                    .add(TTMModifierIds.gungnir, TTMModifierIds.youngscarletmoon, TTMModifierIds.crimsonfantasy)//蕾米
                    .add(TTMModifierIds.laevatein, TTMModifierIds.underredmoon, TTMModifierIds.andthentherewerenone)//芙兰
                    .add(TTMModifierIds.devillibrarian)//姆Q&小恶魔
                    .add(TTMModifierIds.everbrightscarlettower, TTMModifierIds.redblacktree)//小恶魔
                    .add(TTMModifierIds.overpowermq, TTMModifierIds.sevenluminarieswizard)//姆Q
                    .add(TTMModifierIds.killerdoll, TTMModifierIds.vanhelsingprogeny, TTMModifierIds.jacktheripper, TTMModifierIds.perfectandelegant, TTMModifierIds.pocketwatchofblood, TTMModifierIds.theworld)//十六夜咲夜
                    .add(TTMModifierIds.rainbowtaichi, TTMModifierIds.fanghuaxuanlan, TTMModifierIds.bengshancaiji);//红美铃
        tag(TinkerTags.Modifiers.OVERSLIME_FRIEND)//黏液之友
                    .add(TTMModifierIds.overpowermq)
                    .add(TTMModifierIds.overrose)
                    .add(TTMModifierIds.overthinking);
        tag(TagsRegistry.ModifiersTag.SilverModifier)//银质
                    .add(TTMModifierIds.jacktheripper);
        tag(TinkerTags.Modifiers.PROTECTION_DEFENSE)//保护
                    .add(TTMModifierIds.thebloodofoni);
        tag(TagsRegistry.ModifiersTag.Chireiden)//地灵殿
                .add(TTMModifierIds.overrose, TTMModifierIds.missmary, TTMModifierIds.koishiseye, TTMModifierIds.loveburiedembers, TTMModifierIds.idliberation, TTMModifierIds.superegoinhibition)//恋恋
                .add(TTMModifierIds.tsuchigumo, TTMModifierIds.fieldmiasma)//山女
                .add(TTMModifierIds.welldestructor)//小桶
                .add(TTMModifierIds.triplefatality, TTMModifierIds.thebloodofoni, TTMModifierIds.thepowerofmountain)//勇仪
                .add(TTMModifierIds.ushinokokumairi, TTMModifierIds.ushinokokumairishichi, TTMModifierIds.greeneyes)//帕露西
                .add(TTMModifierIds.tokamak, TTMModifierIds.undergroundsun, TTMModifierIds.thousandtrillionflare, TTMModifierIds.hellcrow)//阿空
                .add(TTMModifierIds.terriblesouvenir, TTMModifierIds.overthinking)//觉
                .add(TTMModifierIds.hellstrafficaccident, TTMModifierIds.rekindlingofdeadashes);//猫燐
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier Tag Provider";
    }
}
