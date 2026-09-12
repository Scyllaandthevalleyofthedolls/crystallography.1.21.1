package com.fionaapple.crystallography;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/** Client entry point; this class is never loaded on a dedicated server. */
@Mod(value = Crystallography.MODID, dist = Dist.CLIENT)
public class CrystallographyClient {
    public CrystallographyClient(ModContainer modContainer) {
        Crystallography.LOGGER.info("Crystallography client setup");
    }
}
