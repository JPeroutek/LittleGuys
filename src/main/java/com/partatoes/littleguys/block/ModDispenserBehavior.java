package com.partatoes.littleguys.block;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.item.ModItems;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;

import java.util.Arrays;
import java.util.List;

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
                        }
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

    public static ItemDispenserBehavior DYE_LITTLEGUY_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            List<LittleGuyEntity> list = pointer.world().getEntitiesByClass(LittleGuyEntity.class, new Box(blockPos), entity -> true);
            if (list.isEmpty()) {
                return super.dispenseSilently(pointer, stack);
            } else {
                LittleGuyEntity littleGuyEntity = list.getFirst();

                ItemStack itemStack = stack.split(1);
                DyeColor color = ((DyeItem) itemStack.getItem()).getColor();

                littleGuyEntity.setColor(color);
                littleGuyEntity.setIsNeutral(false);

                return stack;
            }
        }
    };

    public static ItemDispenserBehavior CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            List<LittleGuyEntity> list = pointer.world().getEntitiesByClass(LittleGuyEntity.class, new Box(blockPos), entity -> !entity.isNeutral());
            if (list.isEmpty()) {
                return super.dispenseSilently(pointer, stack);
            } else {
                for (LittleGuyEntity littleGuyEntity : list) {
                    littleGuyEntity.setColor(DyeColor.WHITE);
                    littleGuyEntity.setIsNeutral(true);
                }
                return stack;
            }
        }
    };


    public static void registerDispenserBehaviors() {
        LittleGuys.LOGGER.info("Registering dispenser behaviors for " + LittleGuys.MOD_ID);

        // Register code for dispensing the Little Guys
        DispenserBlock.registerBehavior(ModItems.LITTLEGUY_ITEM, LITTLEGUY_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(ModItems.LITTLEHORSE_ITEM, LITTLEGUY_DISPENSER_BEHAVIOR);
        for (Item spawnEggItem : ModItems.LITTLEGUY_COLORS.values()) {
            DispenserBlock.registerBehavior(spawnEggItem, LITTLEGUY_DISPENSER_BEHAVIOR);
        }

        // Register code for dyeing little guys using dispensers
        for (DyeColor color : DyeColor.values()) {
            DyeItem dye = DyeItem.byColor(color);
            DispenserBlock.registerBehavior(dye, DYE_LITTLEGUY_DISPENSER_BEHAVIOR);
        }

        // Register code for cleaning Little Guys using sponges and dispensers
        DispenserBlock.registerBehavior(Items.SPONGE, CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(Items.WET_SPONGE, CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR);
    }
}
