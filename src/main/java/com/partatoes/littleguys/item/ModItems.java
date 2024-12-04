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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItems {

    public static final Identifier LITTLEGUY_ITEM_ID = Identifier.of(LittleGuys.MOD_ID, "littleguy_item");
    public static final Item LITTLEGUY_ITEM = registerItem(
            LITTLEGUY_ITEM_ID,
            new SpawnEggItem(ModEntities.LITTLEGUY_ENTITY,
                    createItemSettings(LITTLEGUY_ITEM_ID)));

    public static final Identifier LITTLEHORSE_ITEM_ID = Identifier.of(LittleGuys.MOD_ID, "littlehorse_item");
    public static final Item LITTLEHORSE_ITEM = registerItem(
            LITTLEHORSE_ITEM_ID,
            new SpawnEggItem(ModEntities.LITTLEHORSE_ENTITY,
                    createItemSettings(LITTLEHORSE_ITEM_ID)));

    public static final Identifier SOULSAND_PILE_ID = Identifier.of(LittleGuys.MOD_ID, "soulsand_pile");
    public static final Item SOULSAND_PILE_ITEM = registerItem(SOULSAND_PILE_ID, new Item(createItemSettings(SOULSAND_PILE_ID)));

    public static final BiMap<DyeColor, Item> LITTLEGUY_COLORS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                (color) -> color,
                (color) -> registerItem(createLittleGuyColorId((DyeColor) color), new LittleGuySpawnEggItem(ModEntities.COLOR_LITTLEGUY_BIMAP.get(color), color, createItemSettings(createLittleGuyColorId(color)))),
                (a, b) -> a,
                HashBiMap::create));

    @NotNull
    private static Identifier createLittleGuyColorId(DyeColor color) {
        return Identifier.of(LittleGuys.MOD_ID, "littleguy_color_" + color.asString());
    }

    public static Item registerItem(Identifier id, Item item) {
        return Registry.register(Registries.ITEM, id, item);
    }
    public static void registerModItems() {
        LittleGuys.LOGGER.info("Registering items for " + LittleGuys.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }

    private static Item.Settings createItemSettings(Identifier id) {
        return new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id));
    }

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(SOULSAND_PILE_ITEM);
    }
}
