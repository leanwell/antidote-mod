package com.leanwell.antidote.store;

import net.minecraft.world.item.ItemStack;

public class VendingProduct {
    public enum Category {
        ANTIDOTES,
        INGREDIENTS,
        GOD_TIER
    }

    private final int id;
    private final ItemStack itemStack;
    private final int emeraldCost;
    private final int diamondCost;
    private final Category category;
    private final String description;

    public VendingProduct(int id, ItemStack itemStack, int emeraldCost, int diamondCost, Category category, String description) {
        this.id = id;
        this.itemStack = itemStack;
        this.emeraldCost = emeraldCost;
        this.diamondCost = diamondCost;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    public int getEmeraldCost() {
        return emeraldCost;
    }

    public int getDiamondCost() {
        return diamondCost;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
