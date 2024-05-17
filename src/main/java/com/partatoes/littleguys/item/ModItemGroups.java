package com.partatoes.littleguys.item;

import com.partatoes.littleguys.LittleGuys;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup LITTLEGUYS_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(LittleGuys.MOD_ID, "little-guys"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.littleguys"))
                    .icon(() -> new ItemStack(ModItems.LITTLEGUY_ITEM))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.LITTLEGUY_ITEM);
                        entries.add(ModItems.SOULSAND_PILE_ITEM);
                    }).build());

    public static void registerItemGroups() {
        LittleGuys.LOGGER.info("Registering Item Groups for " + LittleGuys.MOD_ID);
    }
}
