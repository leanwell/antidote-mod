package com.leanwell.antidote.store;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class VendingProduct {
    public enum Category {
        ANTIDOTES,
        INGREDIENTS,
        GOD_TIER
    }

    private final int id;
    private final Item item;
    private final int defaultEmeraldCost;
    private final int defaultDiamondCost;
    private int emeraldCost;
    private int diamondCost;
    private final Category category;
    private final String description;

    public VendingProduct(int id, ItemLike itemLike, int emeraldCost, int diamondCost, Category category, String description) {
        if (itemLike == null) {
            throw new IllegalArgumentException("Cannot create VendingProduct with null ItemLike for id " + id + " (" + description + ")");
        }
        this.id = id;
        this.item = itemLike.asItem();
        this.defaultEmeraldCost = emeraldCost;
        this.defaultDiamondCost = diamondCost;
        this.emeraldCost = emeraldCost;
        this.diamondCost = diamondCost;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return new ItemStack(item);
    }

    public int getDefaultEmeraldCost() {
        return defaultEmeraldCost;
    }

    public int getDefaultDiamondCost() {
        return defaultDiamondCost;
    }

    public int getEmeraldCost() {
        return emeraldCost;
    }

    public int getDiamondCost() {
        return diamondCost;
    }

    public void setCosts(int emeraldCost, int diamondCost) {
        this.emeraldCost = Math.max(0, emeraldCost);
        this.diamondCost = Math.max(0, diamondCost);
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
