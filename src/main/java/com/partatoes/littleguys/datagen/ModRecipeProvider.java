package com.partatoes.littleguys.datagen;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider  extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LITTLEGUY_ITEM, 4)
                .pattern("S")
                .pattern("C")
                .input('S', Blocks.SOUL_SAND)
                .input('C', Blocks.CLAY)
                .criterion(hasItem(Blocks.SOUL_SAND), conditionsFromItem(Blocks.SOUL_SAND))
                .criterion(hasItem(Blocks.CLAY), conditionsFromItem(Blocks.CLAY))
                .offerTo(exporter, new Identifier(LittleGuys.MOD_ID, "littleguys_from_clay_and_soulsand"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LITTLEGUY_ITEM, 1)
                .pattern("s")
                .pattern("c")
                .input('s', ModItems.SOULSAND_PILE_ITEM)
                .input('c', Items.CLAY_BALL)
                .criterion(hasItem(ModItems.SOULSAND_PILE_ITEM), conditionsFromItem(ModItems.SOULSAND_PILE_ITEM))
                .criterion(hasItem(Items.CLAY_BALL), conditionsFromItem(Items.CLAY_BALL))
                .offerTo(exporter, new Identifier(LittleGuys.MOD_ID, "littleguy_from_clay_ball_and_soulsand_pile"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LITTLEHORSE_ITEM, 1)
                .pattern("s  ")
                .pattern("cdc")
                .pattern("c c")
                .input('s', ModItems.SOULSAND_PILE_ITEM)
                .input('c', Items.CLAY_BALL)
                .input('d', Items.SADDLE)
                .criterion(hasItem(ModItems.SOULSAND_PILE_ITEM), conditionsFromItem(ModItems.SOULSAND_PILE_ITEM))
                .criterion(hasItem(Items.CLAY_BALL), conditionsFromItem(Items.CLAY_BALL))
                .criterion(hasItem(Items.SADDLE), conditionsFromItem(Items.SADDLE))
                .offerTo(exporter, new Identifier(LittleGuys.MOD_ID, "littlehorse_from_clay_balls_and_soulsand_pile"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SOULSAND_PILE_ITEM, 4)
                .input(Blocks.SOUL_SAND)
                .criterion(hasItem(Blocks.SOUL_SAND), conditionsFromItem(Blocks.SOUL_SAND))
                .offerTo(exporter);

        offer2x2CompactingRecipe(exporter, RecipeCategory.MISC, Blocks.SOUL_SAND, ModItems.SOULSAND_PILE_ITEM);

        ModItems.LITTLEGUY_COLORS.forEach((dc, item) -> {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, item)
                    .input(ModItems.LITTLEGUY_ITEM)
                    .input(DyeItem.byColor(dc))
                    .criterion(hasItem(DyeItem.byColor(dc)), conditionsFromItem(DyeItem.byColor(dc)))
                    .criterion(hasItem(ModItems.LITTLEGUY_ITEM), conditionsFromItem(ModItems.LITTLEGUY_ITEM))
                    .offerTo(exporter);
        });
    }
}
