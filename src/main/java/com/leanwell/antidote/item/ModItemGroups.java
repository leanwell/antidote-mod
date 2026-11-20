package com.leanwell.antidote.item;

import com.leanwell.antidote.Antidote;
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
                    // Add God Antidote at the top
                    entries.accept(ModItems.GOD_ANTIDOTE);
                    // Add all specific condition antidotes
                    for (AntidoteItem item : ModItems.getAll().values()) {
                        if (item != ModItems.GOD_ANTIDOTE) {
                            entries.accept(item);
                        }
                    }
                })
                .build()
        );

        Antidote.LOGGER.info("Registered Antidotes Creative Tab.");
    }
}
