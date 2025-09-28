package com.goldkl.touhoutinkermodifier.data;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.material.TTMMaterialIds;
import com.goldkl.touhoutinkermodifier.registries.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.partbuilder.recycle.PartBuilderToolRecycleBuilder;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;

import java.util.function.Consumer;

public class TTMRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
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
