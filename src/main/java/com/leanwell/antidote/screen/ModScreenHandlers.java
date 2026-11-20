package com.leanwell.antidote.screen;

import com.leanwell.antidote.Antidote;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlers {
    public static MenuType<MedicalVendingScreenHandler> MEDICAL_VENDING;

    public static void register() {
        ResourceKey<MenuType<?>> key = ResourceKey.create(
            Registries.MENU,
            Antidote.id("medical_vending")
        );

        MEDICAL_VENDING = Registry.register(
            BuiltInRegistries.MENU,
            key,
            new MenuType<>(MedicalVendingScreenHandler::new, FeatureFlagSet.of())
        );

        Antidote.LOGGER.info("Registered Medical Vending MenuType.");
    }
}
