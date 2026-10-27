package com.partatoes.littleguys.datagen;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
        return new RecipeProvider(wrapperLookup, recipeExporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.COMBAT, ModItems.LITTLEGUY_ITEM, 4)
                        .pattern("S")
                        .pattern("C")
                        .define('S', Blocks.SOUL_SAND)
                        .define('C', Blocks.CLAY)
                        .unlockedBy(getHasName(Blocks.SOUL_SAND), has(Blocks.SOUL_SAND))
                        .unlockedBy(getHasName(Blocks.CLAY), has(Blocks.CLAY))
                        .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguys_from_clay_and_soulsand")));

                shaped(RecipeCategory.COMBAT, ModItems.LITTLEGUY_ITEM, 1)
                        .pattern("s")
                        .pattern("c")
                        .define('s', ModItems.SOULSAND_PILE_ITEM)
                        .define('c', Items.CLAY_BALL)
                        .unlockedBy(getHasName(ModItems.SOULSAND_PILE_ITEM), has(ModItems.SOULSAND_PILE_ITEM))
                        .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                        .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguy_from_clay_ball_and_soulsand_pile")));

                shaped(RecipeCategory.COMBAT, ModItems.LITTLEHORSE_ITEM, 1)
                        .pattern("s  ")
                        .pattern("cdc")
                        .pattern("c c")
                        .define('s', ModItems.SOULSAND_PILE_ITEM)
                        .define('c', Items.CLAY_BALL)
                        .define('d', Items.SADDLE)
                        .unlockedBy(getHasName(ModItems.SOULSAND_PILE_ITEM), has(ModItems.SOULSAND_PILE_ITEM))
                        .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                        .unlockedBy(getHasName(Items.SADDLE), has(Items.SADDLE))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.SOULSAND_PILE_ITEM, 4)
                        .requires(Blocks.SOUL_SAND)
                        .unlockedBy(getHasName(Blocks.SOUL_SAND), has(Blocks.SOUL_SAND))
                        .save(output);

                twoByTwoPacker(RecipeCategory.MISC, Blocks.SOUL_SAND, ModItems.SOULSAND_PILE_ITEM);

                ModItems.LITTLEGUY_COLORS.forEach((dc, item) -> shapeless(RecipeCategory.MISC, item)
                        .requires(ModItems.LITTLEGUY_ITEM)
                        .requires(dyeItem(dc))
                        .unlockedBy(getHasName(dyeItem(dc)), has(dyeItem(dc)))
                        .unlockedBy(getHasName(ModItems.LITTLEGUY_ITEM), has(ModItems.LITTLEGUY_ITEM))
                        .save(output));
            }
        };
    }

    private static Item dyeItem(DyeColor color) {
        return BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(color.getSerializedName() + "_dye"));
    }

    @Override
    public String getName() {
        return "LittleGuys Recipes";
    }
}
