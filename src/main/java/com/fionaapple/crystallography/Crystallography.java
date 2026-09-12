package com.fionaapple.crystallography;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/** Common entry point for the Crystallography mod. */
@Mod(Crystallography.MODID)
public class Crystallography {
    public static final String MODID = "crystallography";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<net.minecraft.world.level.levelgen.feature.Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, MODID);

    public static final Supplier<Block> SHALE = BLOCKS.register("shale", () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0F, 3.0F)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)));
    public static final Supplier<Item> SHALE_ITEM = ITEMS.register("shale",
            () -> new BlockItem(SHALE.get(), new Item.Properties()));
    public static final Supplier<Item> RAW_SHALE = ITEMS.register("raw_shale",
            () -> new Item(new Item.Properties()));
    public static final Supplier<CreativeModeTab> CRYSTALLOGRAPHY_TAB = CREATIVE_MODE_TABS.register("crystallography",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.crystallography"))
                    .icon(() -> SHALE_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(SHALE_ITEM.get());
                        output.accept(RAW_SHALE.get());
                    })
                    .build());

    public static final Supplier<net.minecraft.world.level.levelgen.feature.Feature<net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration>> SHALE_VEIN =
            FEATURES.register("shale_vein", () -> new ShaleVeinFeature(net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.CODEC));

    public Crystallography(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        FEATURES.register(modEventBus);
        LOGGER.info("Crystallography is loading");
    }
}
