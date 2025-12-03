package com.partatoes.littleguys.block;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.item.ModItems;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;

public class ModDispenserBehavior {
    public static ItemDispenserBehavior LITTLEGUY_DISPENSER_BEHAVIOR = new ItemDispenserBehavior(){
        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            Direction direction = pointer.state().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack);
            try {
                if (entityType != null) {
                    var entity =  entityType.spawnFromItemStack(pointer.world(), stack, null, pointer.pos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                    if (entity != null) {
                        if (entity instanceof LittleGuyEntity littleGuyEntity) {
                            if (stack.getItem() instanceof LittleGuySpawnEggItem eggItem) {
                                littleGuyEntity.setColor(eggItem.color);
                                littleGuyEntity.setIsNeutral(false);
                            }
                        } //else {
//
//                        }
//                        if (stack.getItem() instanceof LittleGuySpawnEggItem eggItem) {
//                            entity.setColor(eggItem.color);
//                            entity.setIsNeutral(false);
//                        }
                    }
                }

            } catch (Exception exception) {
                LittleGuys.LOGGER.error("Error while dispensing spawn egg from dispenser at {} {}", (Object)pointer.pos(), (Object)exception);
                return ItemStack.EMPTY;
            }
            stack.decrement(1);
            pointer.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };
    public static void registerDispenserBehaviors() {
        LittleGuys.LOGGER.info("Registering dispenser behaviors for " + LittleGuys.MOD_ID);

        DispenserBlock.registerBehavior(ModItems.LITTLEGUY_ITEM, LITTLEGUY_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(ModItems.LITTLEHORSE_ITEM, LITTLEGUY_DISPENSER_BEHAVIOR);

        for (Item spawnEggItem : ModItems.LITTLEGUY_COLORS.values()) {
            DispenserBlock.registerBehavior(spawnEggItem, LITTLEGUY_DISPENSER_BEHAVIOR);
        }
    }
}
