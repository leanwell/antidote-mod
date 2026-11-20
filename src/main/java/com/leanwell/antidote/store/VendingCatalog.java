package com.leanwell.antidote.store;

import com.leanwell.antidote.item.AntidoteItem;
import com.leanwell.antidote.item.DisabilityType;
import com.leanwell.antidote.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VendingCatalog {
    private static final List<VendingProduct> PRODUCTS = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        int id = 0;

        // 1. Finished Antidotes (1.5x ingredient cost: 48 Emeralds, 0 Diamonds)
        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;
            AntidoteItem item = ModItems.get(type);
            if (item != null) {
                PRODUCTS.add(new VendingProduct(
                    id++,
                    item,
                    48,
                    0,
                    VendingProduct.Category.ANTIDOTES,
                    "Cures " + type.getDisplayName()
                ));
            }
        }

        // 2. Raw Cure Ingredients (32 Emeralds each, 0 Diamonds)
        addIngredient(id++, Items.GOLDEN_CARROT, 32, "Blindness Ingredient");
        addIngredient(id++, Items.SPIDER_EYE, 32, "Blindness Ingredient");
        addIngredient(id++, Items.GLOWSTONE_DUST, 32, "Color Blindness Ingredient");
        addIngredient(id++, Items.INK_SAC, 32, "Color Blindness Ingredient");
        addIngredient(id++, Items.NOTE_BLOCK, 32, "Deafness / Mute / Tourette's Ingredient");
        addIngredient(id++, Items.WOOL.white(), 32, "Deafness / Insomnia Ingredient");
        addIngredient(id++, Items.HONEYCOMB, 32, "Diabetes / Allergy Ingredient");
        addIngredient(id++, Items.APPLE, 32, "Diabetes / Lactose Ingredient");
        addIngredient(id++, Items.GOLD_NUGGET, 32, "Schizophrenia Ingredient");
        addIngredient(id++, Items.FERMENTED_SPIDER_EYE, 32, "Schizophrenia Ingredient");
        addIngredient(id++, Items.FEATHER, 32, "Muteness / Asthma / Vertigo Ingredient");
        addIngredient(id++, Items.HONEY_BOTTLE, 32, "Seizures / Cerebral Palsy Ingredient");
        addIngredient(id++, Items.GLOW_BERRIES, 32, "Seizures Ingredient");
        addIngredient(id++, Items.GLISTERING_MELON_SLICE, 32, "Cancer Ingredient");
        addIngredient(id++, Items.GHAST_TEAR, 36, "Cancer Ingredient");
        addIngredient(id++, Items.CLOCK, 32, "OCD Ingredient");
        addIngredient(id++, Items.COMPASS, 32, "OCD / Vertigo Ingredient");
        addIngredient(id++, Items.SHIELD, 32, "Amputated Offhand Ingredient");
        addIngredient(id++, Items.SLIME_BALL, 32, "Amputated Limb Ingredient");
        addIngredient(id++, Items.LEATHER_BOOTS, 32, "Amputated Leg Ingredient");
        addIngredient(id++, Items.BOOK, 32, "Dyslexia / Dementia Ingredient");
        addIngredient(id++, Items.GLOW_INK_SAC, 32, "Dyslexia Ingredient");
        addIngredient(id++, Items.BONE_MEAL, 32, "Short Stature Ingredient");
        addIngredient(id++, Items.RED_MUSHROOM, 32, "Short Stature Ingredient");
        addIngredient(id++, Items.CHEST, 32, "Dementia / Kleptomania Ingredient");
        addIngredient(id++, Items.PUFFERFISH, 32, "Asthma Ingredient");
        addIngredient(id++, Items.REDSTONE, 32, "Narcolepsy / ADHD Ingredient");
        addIngredient(id++, Items.COCOA_BEANS, 32, "Narcolepsy Ingredient");
        addIngredient(id++, Items.SWEET_BERRIES, 32, "Parkinson's / Insomnia Ingredient");
        addIngredient(id++, Items.IRON_INGOT, 32, "Parkinson's / Brittle Bones Ingredient");
        addIngredient(id++, Items.DANDELION, 32, "Pollen Allergy Ingredient");
        addIngredient(id++, Items.MAGMA_CREAM, 32, "Arthritis Ingredient");
        addIngredient(id++, Items.BLAZE_POWDER, 32, "Arthritis Ingredient");
        addIngredient(id++, Items.BONE_BLOCK, 32, "Brittle Bones Ingredient");
        addIngredient(id++, Items.COPPER_INGOT, 32, "ADHD Ingredient");
        addIngredient(id++, Items.SUGAR, 32, "Tourette's Ingredient");
        addIngredient(id++, Items.CARVED_PUMPKIN, 32, "Prosopagnosia Ingredient");
        addIngredient(id++, Items.BAMBOO, 32, "Scoliosis Ingredient");
        addIngredient(id++, Items.IRON_CHAIN, 32, "Scoliosis Ingredient");
        addIngredient(id++, Items.GOLD_INGOT, 32, "Kleptomania Ingredient");
        addIngredient(id++, Items.MILK_BUCKET, 32, "Lactose Intolerance Ingredient");
        addIngredient(id++, Items.SPYGLASS, 36, "Anosognosia Ingredient");

        // 3. God Tier (Emeralds + Diamonds as requested)
        PRODUCTS.add(new VendingProduct(
            id++,
            Items.NETHER_STAR,
            100,
            10,
            VendingProduct.Category.GOD_TIER,
            "Nether Star (God Antidote Ingredient)"
        ));

        PRODUCTS.add(new VendingProduct(
            id++,
            Items.TOTEM_OF_UNDYING,
            120,
            20,
            VendingProduct.Category.GOD_TIER,
            "Totem of Undying (God Antidote Ingredient)"
        ));

        PRODUCTS.add(new VendingProduct(
            id++,
            ModItems.GOD_ANTIDOTE,
            200,
            20,
            VendingProduct.Category.GOD_TIER,
            "God Antidote (Cures All Active Conditions)"
        ));

        initialized = true;
    }

    private static void addIngredient(int id, ItemLike item, int emeraldCost, String desc) {
        PRODUCTS.add(new VendingProduct(
            id,
            item,
            emeraldCost,
            0,
            VendingProduct.Category.INGREDIENTS,
            desc
        ));
    }

    public static List<VendingProduct> getAll() {
        if (!initialized) init();
        return Collections.unmodifiableList(PRODUCTS);
    }

    public static VendingProduct get(int id) {
        if (!initialized) init();
        if (id >= 0 && id < PRODUCTS.size()) {
            return PRODUCTS.get(id);
        }
        return null;
    }

    public static List<VendingProduct> getByCategory(VendingProduct.Category category) {
        if (!initialized) init();
        List<VendingProduct> result = new ArrayList<>();
        for (VendingProduct p : PRODUCTS) {
            if (p.getCategory() == category) {
                result.add(p);
            }
        }
        return result;
    }
}
