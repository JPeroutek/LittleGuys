package com.partatoes.littleguys.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItems {

    public static final Item LITTLEGUY_ITEM = registerItem("littleguy_item", new SpawnEggItem(ModEntities.LITTLEGUY_ENTITY, 13158600, 13158600, new Item.Settings()));
    public static final Item LITTLEHORSE_ITEM = registerItem("littlehorse_item", new SpawnEggItem(ModEntities.LITTLEHORSE_ENTITY, 13158600, 13158600, new Item.Settings()));
    public static final Item SOULSAND_PILE_ITEM = registerItem("soulsand_pile", new Item(new Item.Settings()));

    public static final BiMap<DyeColor, Item> LITTLEGUY_COLORS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                (color) -> color,
                (color) -> registerItem("littleguy_color_" + ((DyeColor) color).asString(), new LittleGuySpawnEggItem(ModEntities.COLOR_LITTLEGUY_BIMAP.get(color), color, new Item.Settings())),
                (a, b) -> a,
                HashBiMap::create));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(LittleGuys.MOD_ID, name), item);
    }
    public static void registerModItems() {
        LittleGuys.LOGGER.info("Registering items for " + LittleGuys.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(SOULSAND_PILE_ITEM);
    }
}
