package com.fionaapple.crystallography;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.util.RandomSource;

/** Places a small, shallow vein only when the shoreline is close by. */
public final class ShaleVeinFeature extends Feature<NoneFeatureConfiguration> {
    public ShaleVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int placed = 0;

        for (int attempt = 0; attempt < 24; attempt++) {
            int x = origin.getX() + random.nextInt(9) - 4;
            int z = origin.getZ() + random.nextInt(9) - 4;
            int y = surfaceY - random.nextInt(4);
            pos.set(x, y, z);

            if (isNearWater(level, pos) && isReplaceable(level, pos)) {
                level.setBlock(pos, Crystallography.SHALE.get().defaultBlockState(), 2);
                placed++;
            }
        }
        return placed > 0;
    }

    private static boolean isReplaceable(LevelAccessor level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.SAND)
                || state.is(Blocks.CLAY);
    }

    private static boolean isNearWater(LevelAccessor level, BlockPos center) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        for (int dx = -6; dx <= 6; dx++) {
            for (int dz = -6; dz <= 6; dz++) {
                if (Math.abs(dx) + Math.abs(dz) > 7) continue;
                for (int dy = -3; dy <= 3; dy++) {
                    check.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (level.getFluidState(check).is(net.minecraft.world.level.material.Fluids.WATER)) return true;
                }
            }
        }
        return false;
    }
}
