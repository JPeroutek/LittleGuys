package com.partatoes.littleguys.item.custom;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class LittleGuySpawnEggItem extends SpawnEggItem {
    private final DyeColor color;
    public LittleGuySpawnEggItem(EntityType<? extends MobEntity> type, DyeColor color, Settings settings) {
        super(type, 0, 0, settings);
        this.color = color;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        EntityType<?> entityType;
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof Spawner) {
            Spawner spawner = (Spawner)((Object)blockEntity);
            entityType = this.getEntityType(itemStack);
            spawner.setEntityType(entityType, world.getRandom());
            world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
            world.emitGameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            itemStack.decrement(1);
            return ActionResult.CONSUME;
        }
        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
        entityType = this.getEntityType(itemStack);
        if (entityType.spawnFromItemStack((ServerWorld)world, itemStack, context.getPlayer(), blockPos2, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP) != null) {
            itemStack.decrement(1);
            world.emitGameEvent((Entity)context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = SpawnEggItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        }
        if (!(world instanceof ServerWorld)) {
            return TypedActionResult.success(itemStack);
        }
        BlockHitResult blockHitResult2 = blockHitResult;
        BlockPos blockPos = blockHitResult2.getBlockPos();
        if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
            return TypedActionResult.pass(itemStack);
        }
        if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos, blockHitResult2.getSide(), itemStack)) {
            return TypedActionResult.fail(itemStack);
        }
        EntityType<?> entityType = this.getEntityType(itemStack);
        LittleGuyEntity entity = (LittleGuyEntity) entityType.spawnFromItemStack((ServerWorld)world, itemStack, user, blockPos, SpawnReason.SPAWN_EGG, false, false);

        if (entity == null) {
            return TypedActionResult.pass(itemStack);
        } else {
            entity.setColor(this.color);
        }
        itemStack.decrementUnlessCreative(1, user);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent((Entity)user, GameEvent.ENTITY_PLACE, ((Entity)entity).getPos());
        return TypedActionResult.consume(itemStack);
    }
}
