package com.fionaapple.crystallography;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** A planted gem seed that drains a 6x6 patch below itself before hatching. */
public class GemSeedBlock extends Block {
    public GemSeedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Vanilla random ticks occur roughly once per minute for a particular block.
        // Draining one or two positions per tick gives an average total of about 20-30 minutes.
        int blocksToDrain = 1 + random.nextInt(2);
        for (int i = 0; i < blocksToDrain; i++) {
            BlockPos drained = findDrainableBlock(level, pos, random);
            if (drained == null) {
                break;
            }
            level.setBlock(drained, Crystallography.DRAINED_BLOCK.get().defaultBlockState(), 3);
        }

        int drainedCount = countDrainedBlocks(level, pos);
        if (drainedCount >= 36) {
            spawnPlaceholderMob(level, pos);
        }
    }

    private static BlockPos findDrainableBlock(ServerLevel level, BlockPos seedPos, RandomSource random) {
        // Shuffle the footprint start so growth does not always follow the same pattern.
        int start = random.nextInt(36);
        for (int i = 0; i < 36; i++) {
            int index = (start + i) % 36;
            int x = index % 6 - 3;
            int z = index / 6 - 3;
            BlockPos target = seedPos.below().offset(x, 0, z);
            if (level.getBlockState(target).is(net.minecraft.world.level.block.Blocks.DIRT)
                    || level.getBlockState(target).is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) {
                return target;
            }
        }
        return null;
    }

    private static int countDrainedBlocks(ServerLevel level, BlockPos seedPos) {
        int count = 0;
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                if (level.getBlockState(seedPos.below().offset(x, 0, z))
                        .is(Crystallography.DRAINED_BLOCK.get())) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void spawnPlaceholderMob(ServerLevel level, BlockPos seedPos) {
        var chicken = EntityType.CHICKEN.create(level);
        if (chicken != null) {
            chicken.moveTo(seedPos.getX() + 0.5D, seedPos.getY() + 1.0D, seedPos.getZ() + 0.5D,
                    level.getRandom().nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(chicken);
            level.setBlock(seedPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
