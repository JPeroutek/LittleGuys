package com.partatoes.littleguys.item;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public class LittleGuyItemColorProvider implements ItemColorProvider {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        // This left part is to ensure an alpha of 1
        return (255 << 24) | ModItems.LITTLEGUY_COLORS.inverse().get(stack.getItem()).getSignColor();

    }
}
