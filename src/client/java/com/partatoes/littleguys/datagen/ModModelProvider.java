package com.partatoes.littleguys.datagen;

import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.LITTLEGUY_ITEM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.LITTLEHORSE_ITEM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SOULSAND_PILE_ITEM, ModelTemplates.FLAT_ITEM);

        for (DyeColor color: ModItems.LITTLEGUY_COLORS.keySet()) {
            registerItemWithTextureSourceAndColor(itemModelGenerator, ModItems.LITTLEGUY_COLORS.get(color), ModItems.LITTLEGUY_ITEM, color);
        }
    }

    public void registerItemWithTextureSourceAndColor(ItemModelGenerators itemModelGenerator, Item item, Item textureSource, DyeColor color) {
        itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.tintedModel(ModelLocationUtils.getModelLocation(textureSource), new Dye(color.getTextureDiffuseColor())));
    }
}
