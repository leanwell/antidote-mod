package com.leanwell.antidote.item;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.block.ModBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static CreativeModeTab ANTIDOTES_TAB;

    public static void register() {
        ANTIDOTES_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Antidote.id("antidotes"),
            FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(ModItems.GOD_ANTIDOTE))
                .title(Component.translatable("itemGroup.antidote.antidotes"))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModBlocks.MEDICAL_VENDING_MACHINE_ITEM);
                    
                    // Standard Antidotes
                    entries.accept(ModItems.GOD_ANTIDOTE);
                    for (AntidoteItem item : ModItems.getAll().values()) {
                        if (item != ModItems.GOD_ANTIDOTE) {
                            entries.accept(item);
                        }
                    }

                    // Splash Antidotes
                    entries.accept(ModItems.SPLASH_GOD_ANTIDOTE);
                    for (SplashAntidoteItem item : ModItems.getAllSplash().values()) {
                        if (item != ModItems.SPLASH_GOD_ANTIDOTE) {
                            entries.accept(item);
                        }
                    }

                    // Corrupt Potions
                    entries.accept(ModItems.CORRUPT_GOD_ANTIDOTE);
                    for (CorruptAntidoteItem item : ModItems.getAllCorrupt().values()) {
                        if (item != ModItems.CORRUPT_GOD_ANTIDOTE) {
                            entries.accept(item);
                        }
                    }

                    // Splash Corrupt Potions
                    entries.accept(ModItems.SPLASH_CORRUPT_GOD_ANTIDOTE);
                    for (SplashCorruptAntidoteItem item : ModItems.getAllSplashCorrupt().values()) {
                        if (item != ModItems.SPLASH_CORRUPT_GOD_ANTIDOTE) {
                            entries.accept(item);
                        }
                    }
                })
                .build()
        );

        Antidote.LOGGER.info("Registered Antidotes Creative Tab.");
    }
}
