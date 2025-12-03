package com.partatoes.littleguys.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItems {

    public static final Identifier LITTLEGUY_ITEM_ID = Identifier.of(LittleGuys.MOD_ID, "littleguy_item");
    public static final Item LITTLEGUY_ITEM = registerItem(
            LITTLEGUY_ITEM_ID,
            new SpawnEggItem(
                    createSpawnEggItemSettings(LITTLEGUY_ITEM_ID, ModEntities.LITTLEGUY_ENTITY)));

    public static final Identifier LITTLEHORSE_ITEM_ID = Identifier.of(LittleGuys.MOD_ID, "littlehorse_item");
    public static final Item LITTLEHORSE_ITEM = registerItem(
            LITTLEHORSE_ITEM_ID,
            new SpawnEggItem(
                    createSpawnEggItemSettings(LITTLEHORSE_ITEM_ID, ModEntities.LITTLEHORSE_ENTITY)));

    public static final Identifier SOULSAND_PILE_ID = Identifier.of(LittleGuys.MOD_ID, "soulsand_pile");
    public static final Item SOULSAND_PILE_ITEM = registerItem(SOULSAND_PILE_ID, new Item(createItemSettings(SOULSAND_PILE_ID)));

    public static final BiMap<DyeColor, Item> LITTLEGUY_COLORS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                (color) -> color,
                (color) -> registerItem(createLittleGuyColorId(color), new LittleGuySpawnEggItem(color, createSpawnEggItemSettings(createLittleGuyColorId(color), ModEntities.LITTLEGUY_ENTITY))),
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

    private static Item.Settings createSpawnEggItemSettings(Identifier id, EntityType<?> et) {
        return new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id)).spawnEgg(et);
    }

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(SOULSAND_PILE_ITEM);
    }
}
