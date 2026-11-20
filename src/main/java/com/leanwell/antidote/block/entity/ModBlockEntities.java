package com.leanwell.antidote.block.entity;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class ModBlockEntities {
    public static BlockEntityType<MedicalVendingMachineBlockEntity> MEDICAL_VENDING_MACHINE;

    public static void register() {
        ResourceKey<BlockEntityType<?>> key = ResourceKey.create(
            Registries.BLOCK_ENTITY_TYPE,
            Antidote.id("medical_vending_machine")
        );

        MEDICAL_VENDING_MACHINE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            key,
            new BlockEntityType<>(
                MedicalVendingMachineBlockEntity::new,
                Set.of(ModBlocks.MEDICAL_VENDING_MACHINE)
            )
        );

        Antidote.LOGGER.info("Registered Medical Vending Machine block entity type.");
    }
}
