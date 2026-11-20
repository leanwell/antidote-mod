package com.leanwell.antidote.block.entity;

import com.leanwell.antidote.screen.MedicalVendingScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MedicalVendingMachineBlockEntity extends BlockEntity implements MenuProvider {
    public MedicalVendingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEDICAL_VENDING_MACHINE, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.antidote.medical_vending_machine");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MedicalVendingScreenHandler(
            containerId,
            playerInventory,
            ContainerLevelAccess.create(this.level, this.worldPosition)
        );
    }
}
