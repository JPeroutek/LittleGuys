package com.partatoes.littleguys.item;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item LITTLEGUY_ITEM = registerItem("littleguy_item", new SpawnEggItem(ModEntities.LITTLEGUY_ENTITY, 13158600, 13158600, new Item.Settings()));
    public static final Item SOULSAND_PILE_ITEM = registerItem("soulsand_pile", new Item(new Item.Settings()));

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
