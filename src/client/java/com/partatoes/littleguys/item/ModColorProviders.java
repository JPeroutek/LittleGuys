package com.partatoes.littleguys.item;

import com.partatoes.littleguys.LittleGuys;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;

public class ModColorProviders {
    public static void registerColorProviders() {
        LittleGuys.LOGGER.info("Registering Color Providers for " + LittleGuys.MOD_ID);

        for (Item x : ModItems.LITTLEGUY_COLORS.values()) {
            ColorProviderRegistry.ITEM.register(new LittleGuyItemColorProvider(), x);
        }
    }
}
