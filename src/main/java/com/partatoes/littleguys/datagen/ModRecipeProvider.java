package com.partatoes.littleguys.datagen;

import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider  extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LITTLEGUY_ITEM, 1)
                .pattern("S")
                .pattern("C")
                .input('S', Blocks.SOUL_SAND)
                .input('C', Blocks.CLAY)
                .criterion(hasItem(Blocks.SOUL_SAND), conditionsFromItem(Blocks.SOUL_SAND))
                .criterion(hasItem(Blocks.CLAY), conditionsFromItem(Blocks.CLAY))
                .offerTo(exporter);
    }
}
