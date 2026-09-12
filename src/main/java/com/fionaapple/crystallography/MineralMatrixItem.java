package com.fionaapple.crystallography;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;

/** Mineral matrix item that can be colored with a dye held in the other hand. */
public class MineralMatrixItem extends Item {
    public MineralMatrixItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack matrix = player.getItemInHand(hand);
        ItemStack dyeStack = player.getItemInHand(hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!(dyeStack.getItem() instanceof DyeItem dye)) {
            return InteractionResultHolder.pass(matrix);
        }

        if (!level.isClientSide()) {
            boolean splitStack = matrix.getCount() > 1;
            ItemStack dyedMatrix = splitStack ? matrix.split(1) : matrix;
            dyedMatrix.set(DataComponents.DYED_COLOR,
                    new DyedItemColor(dye.getDyeColor().getTextureDiffuseColor(), true));

            if (splitStack && !player.getInventory().add(dyedMatrix)) {
                player.drop(dyedMatrix, false);
            }
            if (!player.getAbilities().instabuild) {
                dyeStack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(matrix, level.isClientSide());
    }
}
