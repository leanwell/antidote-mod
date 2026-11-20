package com.leanwell.antidote.block;

import com.leanwell.antidote.Antidote;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static Block MEDICAL_VENDING_MACHINE;
    public static Item MEDICAL_VENDING_MACHINE_ITEM;

    public static void register() {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Antidote.id("medical_vending_machine"));
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
            .setId(blockKey)
            .strength(3.5F, 6.0F)
            .requiresCorrectToolForDrops();

        MEDICAL_VENDING_MACHINE = Registry.register(
            BuiltInRegistries.BLOCK,
            blockKey,
            new MedicalVendingMachineBlock(properties)
        );

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Antidote.id("medical_vending_machine"));
        Item.Properties itemProperties = new Item.Properties().setId(itemKey);

        MEDICAL_VENDING_MACHINE_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            new BlockItem(MEDICAL_VENDING_MACHINE, itemProperties)
        );

        Antidote.LOGGER.info("Registered Medical Vending Machine block and item.");
    }
}
