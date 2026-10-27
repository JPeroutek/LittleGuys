package com.partatoes.littleguys.block;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.item.ModItems;
import com.partatoes.littleguys.item.custom.LittleGuySpawnEggItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ModDispenserBehavior {
    public static DefaultDispenseItemBehavior LITTLEGUY_DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior(){
        @Override
        public ItemStack execute(BlockSource pointer, ItemStack stack) {
            Direction direction = pointer.state().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getType(stack);
            try {
                if (entityType != null) {
                    var entity =  entityType.spawn(pointer.level(), stack, null, pointer.pos().relative(direction), EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
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
            stack.shrink(1);
            pointer.level().gameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };

    public static DefaultDispenseItemBehavior DYE_LITTLEGUY_DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
            List<LittleGuyEntity> list = pointer.level().getEntitiesOfClass(LittleGuyEntity.class, new AABB(blockPos), entity -> true);
            DyeColor color = stack.get(DataComponents.DYE);
            if (list.isEmpty() || color == null) {
                return super.execute(pointer, stack);
            } else {
                LittleGuyEntity littleGuyEntity = list.getFirst();

                stack.shrink(1);

                littleGuyEntity.setColor(color);
                littleGuyEntity.setIsNeutral(false);

                return stack;
            }
        }
    };

    public static DefaultDispenseItemBehavior CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
            List<LittleGuyEntity> list = pointer.level().getEntitiesOfClass(LittleGuyEntity.class, new AABB(blockPos), entity -> !entity.isNeutral());
            if (list.isEmpty()) {
                return super.execute(pointer, stack);
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
            Item dye = BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(color.getSerializedName() + "_dye"));
            DispenserBlock.registerBehavior(dye, DYE_LITTLEGUY_DISPENSER_BEHAVIOR);
        }

        // Register code for cleaning Little Guys using sponges and dispensers
        DispenserBlock.registerBehavior(Items.SPONGE, CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(Items.WET_SPONGE, CLEAN_LITTLEGUY_DISPENSER_BEHAVIOR);
    }
}
