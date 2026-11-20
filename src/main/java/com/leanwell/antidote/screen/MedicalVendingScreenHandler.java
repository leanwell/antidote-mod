package com.leanwell.antidote.screen;

import com.leanwell.antidote.block.ModBlocks;
import com.leanwell.antidote.store.VendingCatalog;
import com.leanwell.antidote.store.VendingProduct;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MedicalVendingScreenHandler extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    public MedicalVendingScreenHandler(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public MedicalVendingScreenHandler(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModScreenHandlers.MEDICAL_VENDING, containerId);
        this.access = access;

        // Player Inventory (3 rows x 9 columns) - centered at x=48, y=146
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 48 + col * 18, 146 + row * 18));
            }
        }

        // Player Hotbar (1 row x 9 columns) - centered at x=48, y=204
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 48 + col * 18, 204));
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
        VendingProduct product = VendingCatalog.get(id);
        if (product == null) return false;

        boolean isCreative = player.hasInfiniteMaterials();
        int invEmeralds = countPlayerItem(player, Items.EMERALD);
        int invDiamonds = countPlayerItem(player, Items.DIAMOND);

        com.leanwell.antidote.config.AntidoteConfig config = com.leanwell.antidote.config.AntidoteConfigManager.getConfig();
        double discount = config.getHeroDiscount(player);
        int emeraldCost = config.getDiscountedEmeralds(product.getEmeraldCost(), discount);
        int diamondCost = config.getDiscountedDiamonds(product.getDiamondCost(), discount);

        if (isCreative || (invEmeralds >= emeraldCost && invDiamonds >= diamondCost)) {
            if (!isCreative) {
                deductPlayerItem(player, Items.EMERALD, emeraldCost);
                deductPlayerItem(player, Items.DIAMOND, diamondCost);
            }

            ItemStack productStack = product.getItemStack();
            if (!player.getInventory().add(productStack)) {
                player.level().addFreshEntity(new ItemEntity(
                    player.level(), player.getX(), player.getY(), player.getZ(), productStack
                ));
            }

            this.broadcastChanges();
            playDispenseAudio(player);
            return true;
        }

        return false;
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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index < 27) {
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.MEDICAL_VENDING_MACHINE);
    }
}
