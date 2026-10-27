package com.partatoes.littleguys.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItems {

    public static final Identifier LITTLEGUY_ITEM_ID = Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguy_item");
    public static final Item LITTLEGUY_ITEM = registerItem(
            LITTLEGUY_ITEM_ID,
            new SpawnEggItem(
                    createSpawnEggItemSettings(LITTLEGUY_ITEM_ID, ModEntities.LITTLEGUY_ENTITY)));

    public static final Identifier LITTLEHORSE_ITEM_ID = Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littlehorse_item");
    public static final Item LITTLEHORSE_ITEM = registerItem(
            LITTLEHORSE_ITEM_ID,
            new SpawnEggItem(
                    createSpawnEggItemSettings(LITTLEHORSE_ITEM_ID, ModEntities.LITTLEHORSE_ENTITY)));

    public static final Identifier SOULSAND_PILE_ID = Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "soulsand_pile");
    public static final Item SOULSAND_PILE_ITEM = registerItem(SOULSAND_PILE_ID, new Item(createItemSettings(SOULSAND_PILE_ID)));

    public static final BiMap<DyeColor, Item> LITTLEGUY_COLORS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                (color) -> color,
                (color) -> registerItem(createLittleGuyColorId(color), new LittleGuySpawnEggItem(color, createSpawnEggItemSettings(createLittleGuyColorId(color), ModEntities.LITTLEGUY_ENTITY))),
                (a, b) -> a,
                HashBiMap::create));

    @NotNull
    private static Identifier createLittleGuyColorId(DyeColor color) {
        return Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguy_color_" + color.getSerializedName());
    }

    public static Item registerItem(Identifier id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }
    public static void registerModItems() {
        LittleGuys.LOGGER.info("Registering items for " + LittleGuys.MOD_ID);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }

    private static Item.Properties createItemSettings(Identifier id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id));
    }

    private static Item.Properties createSpawnEggItemSettings(Identifier id, EntityType<?> et) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).spawnEgg(et);
    }

    private static void addItemsToIngredientTabItemGroup(FabricCreativeModeTabOutput entries) {
        entries.accept(SOULSAND_PILE_ITEM);
    }
}
