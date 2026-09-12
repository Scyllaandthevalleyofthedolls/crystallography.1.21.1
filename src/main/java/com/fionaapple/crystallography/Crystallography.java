package com.fionaapple.crystallography;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/** Common entry point for the Crystallography mod. */
@Mod(Crystallography.MODID)
public class Crystallography {
    public static final String MODID = "crystallography";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Crystallography(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Crystallography is loading");
    }
}
