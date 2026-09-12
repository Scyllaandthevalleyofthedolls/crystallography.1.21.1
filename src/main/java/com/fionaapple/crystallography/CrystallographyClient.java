package com.fionaapple.crystallography;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.component.DyedItemColor;

/** Client entry point; this class is never loaded on a dedicated server. */
@Mod(value = Crystallography.MODID, dist = Dist.CLIENT)
public class CrystallographyClient {
    public CrystallographyClient(ModContainer modContainer) {
        Crystallography.LOGGER.info("Crystallography client setup");
        modContainer.getEventBus().addListener(this::registerItemColors);
    }

    private void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColor mineralMatrixColor = (stack, tintIndex) -> tintIndex == 1
                ? DyedItemColor.getOrDefault(stack, 0xFFFFFFFF)
                : 0xFFFFFFFF;
        event.register(mineralMatrixColor, Crystallography.MINERAL_MATRIX.get());
    }
}
