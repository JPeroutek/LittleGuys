package com.partatoes.littleguys.item.custom;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class LittleGuySpawnEggItem extends SpawnEggItem {
    public final DyeColor color;
    public LittleGuySpawnEggItem(DyeColor color, Properties settings) {
        super(settings);
        this.color = color;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        EntityType<?> entityType;
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof Spawner) {
            Spawner spawner = (Spawner)(blockEntity);
            entityType = this.getType(itemStack);
            spawner.setEntityId(entityType, world.getRandom());
            world.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
            world.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }
        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);
        entityType = this.getType(itemStack);
        LittleGuyEntity entity = (LittleGuyEntity) entityType.spawn((ServerLevel) world, itemStack, context.getPlayer(), blockPos2, EntitySpawnReason.SPAWN_ITEM_USE, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
        if (entity != null) {
            itemStack.shrink(1);
            world.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            entity.setColor(this.color);
            entity.setIsNeutral(false);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult blockHitResult = SpawnEggItem.getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (!(world.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
            return InteractionResult.PASS;
        }
        if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
            return InteractionResult.FAIL;
        }
        EntityType<?> entityType = this.getType(itemStack);
        LittleGuyEntity entity = (LittleGuyEntity) entityType.spawn((ServerLevel)world, itemStack, user, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, false, false);

        if (entity == null) {
            return InteractionResult.PASS;
        } else {
            entity.setColor(this.color);
        }
        itemStack.consume(1, user);
        user.awardStat(Stats.ITEM_USED.get(this));
        world.gameEvent(user, GameEvent.ENTITY_PLACE, (entity).position());
        return InteractionResult.CONSUME;
    }
}
