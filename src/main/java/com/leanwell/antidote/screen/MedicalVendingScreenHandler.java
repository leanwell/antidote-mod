package com.leanwell.antidote.screen;

import com.leanwell.antidote.block.ModBlocks;
import com.leanwell.antidote.store.VendingCatalog;
import com.leanwell.antidote.store.VendingProduct;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MedicalVendingScreenHandler extends AbstractContainerMenu {
    public static final int EJECT_BUTTON_ID = 999;
    private final Container container;
    private final ContainerLevelAccess access;
    private final ContainerData data;
    private int emeraldBuffer = 0;
    private int diamondBuffer = 0;
    private boolean updatingBuffers = false;

    public MedicalVendingScreenHandler(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public MedicalVendingScreenHandler(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModScreenHandlers.MEDICAL_VENDING, containerId);
        this.access = access;

        this.container = new SimpleContainer(3) {
            @Override
            public void setChanged() {
                super.setChanged();
                MedicalVendingScreenHandler.this.slotsChanged(this);
            }
        };

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> emeraldBuffer;
                    case 1 -> diamondBuffer;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> emeraldBuffer = value;
                    case 1 -> diamondBuffer = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.addDataSlots(this.data);

        // Slot 0: Emerald Payment Slot (x=178, y=42)
        this.addSlot(new Slot(this.container, 0, 178, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.EMERALD);
            }
        });

        // Slot 1: Diamond Payment Slot (x=214, y=42)
        this.addSlot(new Slot(this.container, 1, 214, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.DIAMOND);
            }
        });

        // Slot 2: Output Chute Slot (x=196, y=70)
        this.addSlot(new Slot(this.container, 2, 196, 70) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player Inventory (3 rows x 9 columns) - centered at x=48
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 48 + col * 18, 146 + row * 18));
            }
        }

        // Player Hotbar (1 row x 9 columns) - centered at x=48
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 48 + col * 18, 204));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (updatingBuffers) return;

        try {
            updatingBuffers = true;
            boolean changed = false;

            ItemStack emeraldStack = this.getSlot(0).getItem();
            if (!emeraldStack.isEmpty() && emeraldStack.is(Items.EMERALD)) {
                this.emeraldBuffer += emeraldStack.getCount();
                this.getSlot(0).set(ItemStack.EMPTY);
                changed = true;
            }

            ItemStack diamondStack = this.getSlot(1).getItem();
            if (!diamondStack.isEmpty() && diamondStack.is(Items.DIAMOND)) {
                this.diamondBuffer += diamondStack.getCount();
                this.getSlot(1).set(ItemStack.EMPTY);
                changed = true;
            }

            if (changed) {
                this.broadcastChanges();
            }
        } finally {
            updatingBuffers = false;
        }
    }

    public static int countPlayerItem(Player player, Item item) {
        if (player == null) return 0;
        int count = 0;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static void deductPlayerItem(Player player, Item item, int amount) {
        if (player == null || amount <= 0) return;
        int remaining = amount;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                int take = Math.min(stack.getCount(), remaining);
                stack.shrink(take);
                remaining -= take;
                if (remaining <= 0) break;
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == EJECT_BUTTON_ID) {
            ejectCoins(player);
            return true;
        }

        VendingProduct product = VendingCatalog.get(id);
        if (product == null) return false;

        boolean isCreative = player.hasInfiniteMaterials();
        int invEmeralds = countPlayerItem(player, Items.EMERALD);
        int invDiamonds = countPlayerItem(player, Items.DIAMOND);
        int totalEmeralds = this.emeraldBuffer + invEmeralds;
        int totalDiamonds = this.diamondBuffer + invDiamonds;

        if (isCreative || (totalEmeralds >= product.getEmeraldCost() && totalDiamonds >= product.getDiamondCost())) {
            if (!isCreative) {
                int emNeeded = product.getEmeraldCost();
                int fromEmBuffer = Math.min(this.emeraldBuffer, emNeeded);
                this.emeraldBuffer -= fromEmBuffer;
                emNeeded -= fromEmBuffer;
                if (emNeeded > 0) {
                    deductPlayerItem(player, Items.EMERALD, emNeeded);
                }

                int diaNeeded = product.getDiamondCost();
                int fromDiaBuffer = Math.min(this.diamondBuffer, diaNeeded);
                this.diamondBuffer -= fromDiaBuffer;
                diaNeeded -= fromDiaBuffer;
                if (diaNeeded > 0) {
                    deductPlayerItem(player, Items.DIAMOND, diaNeeded);
                }
            }

            Slot outputSlot = this.getSlot(2);
            ItemStack currentOutput = outputSlot.getItem();
            ItemStack productStack = product.getItemStack();

            if (currentOutput.isEmpty()) {
                outputSlot.set(productStack);
            } else if (ItemStack.isSameItemSameComponents(currentOutput, productStack)
                && currentOutput.getCount() + productStack.getCount() <= currentOutput.getMaxStackSize()) {
                currentOutput.grow(productStack.getCount());
                outputSlot.set(currentOutput);
            } else {
                if (!player.getInventory().add(productStack)) {
                    player.level().addFreshEntity(new ItemEntity(
                        player.level(), player.getX(), player.getY(), player.getZ(), productStack
                    ));
                }
            }

            this.broadcastChanges();
            playDispenseAudio(player);
            return true;
        }

        return false;
    }

    public void ejectCoins(Player player) {
        if (player.level().isClientSide()) return;

        while (this.emeraldBuffer > 0) {
            int count = Math.min(this.emeraldBuffer, 64);
            this.emeraldBuffer -= count;
            ItemStack refund = new ItemStack(Items.EMERALD, count);
            if (!player.getInventory().add(refund)) {
                player.level().addFreshEntity(new ItemEntity(
                    player.level(), player.getX(), player.getY(), player.getZ(), refund
                ));
            }
        }

        while (this.diamondBuffer > 0) {
            int count = Math.min(this.diamondBuffer, 64);
            this.diamondBuffer -= count;
            ItemStack refund = new ItemStack(Items.DIAMOND, count);
            if (!player.getInventory().add(refund)) {
                player.level().addFreshEntity(new ItemEntity(
                    player.level(), player.getX(), player.getY(), player.getZ(), refund
                ));
            }
        }

        this.broadcastChanges();
    }

    private void playDispenseAudio(Player player) {
        this.access.execute((level, pos) -> {
            level.playSound(
                null,
                pos,
                SoundEvents.DISPENSER_DISPENSE,
                SoundSource.BLOCKS,
                1.0F,
                1.2F
            );
            level.playSound(
                null,
                pos,
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.BLOCKS,
                0.8F,
                1.5F
            );
        });
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        ejectCoins(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index == 2) {
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else if (index == 0 || index == 1) {
                if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (slotStack.is(Items.EMERALD)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.is(Items.DIAMOND)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.moveItemStackTo(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.MEDICAL_VENDING_MACHINE);
    }

    public int getEmeraldBuffer() {
        return this.data.get(0);
    }

    public int getDiamondBuffer() {
        return this.data.get(1);
    }
}
