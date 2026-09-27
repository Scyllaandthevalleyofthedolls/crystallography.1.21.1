package com.fionaapple.crystallography;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

/** Seed item that can be planted on dirt or grass. */
public class GemSeedItem extends Item {
    public GemSeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var placePos = clickedPos.relative(context.getClickedFace());
        var clickedState = level.getBlockState(clickedPos);
        boolean validSoil = clickedState.is(Blocks.DIRT) || clickedState.is(Blocks.GRASS_BLOCK);

        if (context.getClickedFace().getAxis().isHorizontal()
                || clickedState.is(Crystallography.DRAINED_BLOCK.get())
                || !validSoil
                || !level.getBlockState(placePos).canBeReplaced()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.setBlock(placePos, Crystallography.GEM_SEED.get().defaultBlockState(), 3);
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
