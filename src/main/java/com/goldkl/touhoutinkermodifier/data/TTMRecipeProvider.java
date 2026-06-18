package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.material.TTMMaterialIds;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import lombok.Getter;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.partbuilder.ItemPartRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.recycle.PartBuilderToolRecycleBuilder;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Consumer;


public class TTMRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
    private enum ArmorRecipe {
        HELMET(ArmorItem.Type.HELMET, TinkerSmeltery.helmetPlatingCast,3),
        CHESTPLATE(ArmorItem.Type.CHESTPLATE, TinkerSmeltery.chestplatePlatingCast,6),
        LEGGINGS(ArmorItem.Type.LEGGINGS, TinkerSmeltery.leggingsPlatingCast,5),
        BOOTS(ArmorItem.Type.BOOTS, TinkerSmeltery.bootsPlatingCast,2);
        @Getter
        private final ArmorItem.Type type;
        @Getter
        private final CastItemObject cast;
        @Getter
        private final int cost;
        ArmorRecipe(ArmorItem.Type type, CastItemObject cast, int cost) {
            this.type = type;
            this.cast = cast;
            this.cost = cost;
        }
        public String getPlating(){
            return type.getName() + "_plating";
        }
    }
    public TTMRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        //material
        this.addMaterialItems(consumer);
        this.addMaterialSmeltery(consumer);
        //tool
        this.addToolBuildingRecipes(consumer);
        this.addRecycleRecipes(consumer);
        this.addPartRecipes(consumer);
    }
    // material
    private void addMaterialItems(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/materials/";
        //tier 0
        materialRecipe(consumer, TTMMaterialIds.kedama,
                Ingredient.of(ItemsRegistry.kedama),
                1, 1, folder + "kedama");
        for(ArmorRecipe ar : ArmorRecipe .values()){
            ItemStack result = new ItemStack(TinkerToolParts.plating.get(ar.getType()));
            result.getOrCreateTag().putString(IMaterialItem.MATERIAL_TAG, TTMMaterialIds.kedama.toString());
            ItemPartRecipeBuilder
                    .item(TConstruct.getResource(ar.getPlating()), ItemOutput.fromStack(result))
                    .material(TTMMaterialIds.kedama, ar.getCost())
                    .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS), Ingredient.of(ar.getCast().get())))
                    .save(consumer, location("tools/parts/builder/kedama_" + ar.getType().getName() + "_plating"));
        }
        //tier 1
        materialRecipe(consumer, TTMMaterialIds.yinangorb,
                Ingredient.of(ItemsRegistry.yinangorb),
                2, 1, folder + "yinangorb");
        //tier 2
        materialRecipe(consumer, TTMMaterialIds.spellpaper,
                Ingredient.of(ItemsRegistry.spellpaper),
                2, 1, folder + "spellpaper");
        materialRecipe(consumer, TTMMaterialIds.persuasionneedle,
                Ingredient.of(ItemsRegistry.persuasionneedle),
                2, 1, folder + "persuasionneedle");
        materialRecipe(consumer, TTMMaterialIds.konpeito,
                Ingredient.of(ItemsRegistry.konpeito),
                2, 1, folder + "konpeito");
        materialRecipe(consumer, TTMMaterialIds.eighttrigramsfurnace,
                Ingredient.of(ItemsRegistry.eighttrigramsfurnace),
                2, 1, folder + "eighttrigramsfurnace");

    }
    private void addMaterialSmeltery(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/materials/";
    }
    // tool
    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/building/";
        String armorFolder = "tools/armor/";
        toolBuilding(consumer, ItemsRegistry.Spear, folder);
        ToolBuildingRecipeBuilder.toolBuildingRecipe(ItemsRegistry.BrassKnuckles.get())
                .outputSize(2)
                .save(consumer, prefix(ItemsRegistry.BrassKnuckles, folder));
        toolBuilding(consumer, ItemsRegistry.Gohei, folder);
    }

    private void addRecycleRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/recycling/";
        PartBuilderToolRecycleBuilder.tools(SizedIngredient.fromItems(2, ItemsRegistry.BrassKnuckles))
                .save(consumer, location(folder + "brass_knuckles"));
    }

    private void addPartRecipes(Consumer<FinishedRecipe> consumer){
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";
        partRecipes(consumer, ItemsRegistry.GoheiCore, ItemsRegistry.GoheiCoreCast, 2, partFolder, castFolder);

    }
    @Override
    public String getModId() {
        return TouhouTinkerModifier.MODID;
    }
}
