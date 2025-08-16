package com.partatoes.littleguys.datagen;

import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import net.minecraft.client.data.*;
import net.minecraft.client.render.item.tint.DyeTintSource;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.LITTLEGUY_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.LITTLEHORSE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOULSAND_PILE_ITEM, Models.GENERATED);

        for (DyeColor color: ModItems.LITTLEGUY_COLORS.keySet()) {
            registerItemWithTextureSourceAndColor(itemModelGenerator, ModItems.LITTLEGUY_COLORS.get(color), ModItems.LITTLEGUY_ITEM, color);
        }

//        for (Item coloredLittleGuy : ModItems.LITTLEGUY_COLORS.values()) {
////            itemModelGenerator.register(coloredLittleGuy, Models.GENERATED);
//            itemModelGenerator.registerWithTextureSource(coloredLittleGuy, ModItems.LITTLEGUY_ITEM, Models.GENERATED);
//            itemModelGenerator.
//        }
    }

    public void registerItemWithTextureSourceAndColor(ItemModelGenerator itemModelGenerator, Item item, Item textureSource, DyeColor color) {
        itemModelGenerator.output.accept(item, ItemModels.tinted(ModelIds.getItemModelId(textureSource), new TintSource[]{new DyeTintSource(color.getEntityColor())}));
    }
}
