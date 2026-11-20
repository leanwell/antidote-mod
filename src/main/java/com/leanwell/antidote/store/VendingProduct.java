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
    private final int emeraldCost;
    private final int diamondCost;
    private final Category category;
    private final String description;

    public VendingProduct(int id, ItemLike itemLike, int emeraldCost, int diamondCost, Category category, String description) {
        this.id = id;
        this.item = itemLike.asItem();
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
