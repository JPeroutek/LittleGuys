package com.partatoes.littleguys.item;

import com.partatoes.littleguys.LittleGuys;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab LITTLEGUYS_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "little-guys"),
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.littleguys"))
                    .icon(() -> new ItemStack(ModItems.LITTLEGUY_ITEM))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModItems.LITTLEGUY_ITEM);
                        for (Item x : ModItems.LITTLEGUY_COLORS.values()) {
                            entries.accept(x);
                        }
                        entries.accept(ModItems.LITTLEHORSE_ITEM);
                        entries.accept(ModItems.SOULSAND_PILE_ITEM);
                    }).build());

    public static void registerItemGroups() {
        LittleGuys.LOGGER.info("Registering Item Groups for " + LittleGuys.MOD_ID);
    }
}
