package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.module.SpellAffinityModule;
import com.goldkl.touhoutinkermodifier.module.SpellModule;
import com.goldkl.touhoutinkermodifier.predicate.AbstractSpellPredicate;
import com.goldkl.touhoutinkermodifier.predicate.IsSchoolTypeSpellPredicate;
import com.goldkl.touhoutinkermodifier.predicate.IsSpellPredicate;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMPredicate;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.predicate.item.ItemPredicate;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.json.predicate.tool.HasMaterialPredicate;
import slimeknights.tconstruct.library.json.variable.entity.AttributeEntityVariable;
import slimeknights.tconstruct.library.json.variable.entity.ConditionalEntityVariable;
import slimeknights.tconstruct.library.json.variable.entity.EntityVariable;
import slimeknights.tconstruct.library.json.variable.protection.EntityProtectionVariable;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.modules.armor.MaxArmorAttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.ProtectionModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierRequirementsModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static slimeknights.tconstruct.common.TinkerTags.Items.HARVEST;
import static slimeknights.tconstruct.common.TinkerTags.Items.WORN_ARMOR;
import static slimeknights.tconstruct.library.json.math.ModifierFormula.*;
import static slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial.ARMOR_SLOTS;

public class ModifiersProvider extends AbstractModifierProvider implements IConditionBuilder {
    public ModifiersProvider(PackOutput packOutput) {
        super(packOutput);
    }
    @Override
    protected void addModifiers() {
        //识文解字：本居小铃
        buildModifier(TTMModifierIds.bibliophilia)
                .addModule(AttributeModule.builder(AttributeRegistry.SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE)
                    .eachLevel(0.15f));
        //绯色月下：芙兰朵路·斯卡雷特
        buildModifier(TTMModifierIds.underredmoon)
                .addModule(AttributeModule.builder(L2DamageTracker.CRIT_DMG.get(), AttributeModifier.Operation.ADDITION)
                        .slots(EquipmentSlot.MAINHAND)
                        .eachLevel(0.5f))
                .addModule(AttributeModule.builder(L2DamageTracker.CRIT_RATE.get(), AttributeModifier.Operation.ADDITION)
                        .slots(EquipmentSlot.MAINHAND)
                        .eachLevel(0.1f))
                .addModule(AttributeModule.builder(ALObjects.Attributes.ARMOR_SHRED.get(), AttributeModifier.Operation.ADDITION)
                        .slots(EquipmentSlot.MAINHAND)
                        .eachLevel(0.1f))
                .addModule(SpellModule.builder().passMaxlevel(true).spell(SpellsRegistry.worldender.get()).eachLevel(1));
        //七曜魔女：帕秋莉
        buildModifier(TTMModifierIds.sevenluminarieswizard)
                .addModule(AttributeModule.builder(AttributesRegistry.MANA_COST_REDUCTION, AttributeModifier.Operation.MULTIPLY_BASE)
                        .eachLevel(0.075f))
                .addModule(AttributeModule.builder(AttributeRegistry.FIRE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.ICE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.LIGHTNING_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.BLOOD_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.NATURE_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.EVOCATION_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f))
                .addModule(AttributeModule.builder(AttributeRegistry.HOLY_SPELL_POWER, AttributeModifier.Operation.MULTIPLY_BASE)
                        .tooltipStyle(AttributeModule.TooltipStyle.PERCENT)
                        .eachLevel(0.05f));
        //山之佐伯：黑谷山女
        buildModifier(TTMModifierIds.tsuchigumo)
                .addModule(SpellModule.builder().passMaxlevel(true).passConfig(true).spell(SpellRegistry.SPIDER_ASPECT_SPELL.get()).eachLevel(1));
        //瘴气四溢：黑谷山女
        buildModifier(TTMModifierIds.fieldmiasma)
                .addModule(SpellAffinityModule.builder().passConfig(true).level(2)
                        .spell(AbstractSpellPredicate.or(
                                new IsSpellPredicate(SpellRegistry.SPIDER_ASPECT_SPELL.get()),
                                new IsSpellPredicate(SpellRegistry.ACID_ORB_SPELL.get()),
                                new IsSpellPredicate(SpellRegistry.POISON_ARROW_SPELL.get()),
                                new IsSpellPredicate(SpellRegistry.POISON_BREATH_SPELL.get()),
                                new IsSpellPredicate(SpellRegistry.POISON_SPLASH_SPELL.get()))));
        //鬼之血脉：星熊勇仪，伊吹萃香，茨木华扇，（矜羯罗？）
        buildModifier(TTMModifierIds.thebloodofoni)
                .addModule(AttributeModule.builder(Attributes.KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADDITION)
                        .eachLevel(0.125f))
                .addModule(AttributeModule.builder(Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION)
                        .eachLevel(4))
                .addModule(MaxArmorAttributeModule.builder(ALObjects.Attributes.DODGE_CHANCE.get(), AttributeModifier.Operation.MULTIPLY_BASE)
                        .allowBroken()
                        .heldTag(TinkerTags.Items.HELD)
                        .flat(-1f))
                .addModule(ProtectionModule.builder()
                        .source(TTMPredicate.PHYSICAL_DAMAGE)
                        .eachLevel(2f));
        //鲜红幼月：蕾米莉亚
        buildModifier(TTMModifierIds.youngscarletmoon)
                .addModule(ProtectionModule.builder()
                        .source(TTMPredicate.NOT_IGNORE_DAMAGE)
                        .customVariable("spell_power",new EntityProtectionVariable(new AttributeEntityVariable(AttributeRegistry.SPELL_POWER.get()), EntityProtectionVariable.WhichEntity.TARGET, 1))
                        .customVariable("blood_spell_power",new EntityProtectionVariable(new AttributeEntityVariable(AttributeRegistry.BLOOD_SPELL_POWER.get()), EntityProtectionVariable.WhichEntity.TARGET, 1))
                        .customVariable("crouching",new EntityProtectionVariable(new ConditionalEntityVariable(LivingEntityPredicate.CROUCHING,2,1), EntityProtectionVariable.WhichEntity.TARGET, 1))
                        .formula()
                        .variable(LEVEL)
                        .customVariable("spell_power")
                        .customVariable("blood_spell_power")
                        .customVariable("crouching")
                        .multiply()
                        .multiply()
                        .multiply()
                        .variable(VALUE).add()
                        .build());
        //秋日澄空：秋静叶&&秋穰子
        IJsonPredicate<Item> harvest = ItemPredicate.tag(HARVEST);
        IJsonPredicate<Item> armor = ItemPredicate.tag(WORN_ARMOR);
        IJsonPredicate<BlockState> crops = BlockPredicate.or(BlockPredicate.tag(BlockTags.CROPS), BlockPredicate.set(Blocks.MELON));
        buildModifier(TTMModifierIds.autumnsky)
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(EnchantmentModule.builder(Enchantments.BLOCK_FORTUNE).toolItem(harvest).block(crops).level(3).mainHandHarvest(TouhouTinkerModifier.getResource("autumnsky_mainhandharvest")))
                .addModule(EnchantmentModule.builder(Enchantments.BLOCK_FORTUNE).toolItem(armor).block(crops).level(3).armorHarvest(ARMOR_SLOTS));
        //THE WORLD：十六夜咲夜
        IJsonPredicate<MaterialVariantId> sakura = MaterialPredicate.tag(TagsRegistry.MaterialsTag.IzayoiSakuya);
        buildModifier(TTMModifierIds.theworld)
                .addModule(ModifierRequirementsModule.builder().requirement(new HasMaterialPredicate(sakura,-1)).modifierKey(TTMModifierIds.theworld).build())
                .addModule(SpellModule.builder().passMaxlevel(true).passConfig(true).spell(SpellsRegistry.theworld.get()).eachLevel(1));
        //冬之军势：蕾蒂
        buildModifier(TTMModifierIds.armyofthewinter)
                .addModule(SpellAffinityModule.builder().spell(new IsSchoolTypeSpellPredicate(SchoolRegistry.ICE.get())));
        //极限火花：雾雨魔理沙
        buildModifier(TTMModifierIds.masterspark)
                .addModule(SpellModule.builder().passMaxlevel(true).passConfig(true).spell(SpellsRegistry.masterspark.get()).amount(-1,2));
        //普通的魔法使：雾雨魔理沙
        buildModifier(TTMModifierIds.commonmagician)
            .addModule(AttributeModule.builder(PerkAttributes.MAX_MANA.get(), AttributeModifier.Operation.ADDITION).eachLevel(1200))
            .addModule(AttributeModule.builder(PerkAttributes.MANA_REGEN_BONUS.get(), AttributeModifier.Operation.ADDITION).eachLevel(75));
    }
    @Override
    public String getName() {
        return "TouhouTinkerModifier's Modifiers";
    }
    /** Short helper to get a modifier ID */
    private static ModifierId id(String name) {
        return new ModifierId(TouhouTinkerModifier.MODID, name);
    }

    /** Short helper to get a modifier ID */
    private static Pattern pattern(String name) {
        return new Pattern(TouhouTinkerModifier.MODID, name);
    }
}
