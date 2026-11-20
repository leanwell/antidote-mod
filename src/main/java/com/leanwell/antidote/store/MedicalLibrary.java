package com.leanwell.antidote.store;

import com.leanwell.antidote.item.DisabilityType;
import com.leanwell.antidote.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalLibrary {
    public record RecipeEntry(
        Item antidote,
        String conditionName,
        String lore,
        List<Item> ingredients
    ) {}

    private static final List<RecipeEntry> ENTRIES = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        add(ModItems.GOD_ANTIDOTE, "God Antidote", "Cures all disabilities & resets timer",
            List.of(Items.POTION, Items.NETHER_STAR, Items.TOTEM_OF_UNDYING));

        add(ModItems.get(DisabilityType.BLIND), "Blindness", "Cures permanent blindness",
            List.of(Items.POTION, Items.GOLDEN_CARROT, Items.SPIDER_EYE));

        add(ModItems.get(DisabilityType.COLOR_BLIND), "Color Blindness", "Restores vibrant color vision",
            List.of(Items.POTION, Items.GLOWSTONE_DUST, Items.INK_SAC));

        add(ModItems.get(DisabilityType.DEAF), "Deafness", "Restores hearing and sound perception",
            List.of(Items.POTION, Items.NOTE_BLOCK, Items.WOOL.white()));

        add(ModItems.get(DisabilityType.DIABETES), "Diabetes", "Balances blood sugar and food tolerance",
            List.of(Items.POTION, Items.HONEYCOMB, Items.APPLE));

        add(ModItems.get(DisabilityType.SCHIZOPHRENIA), "Schizophrenia", "Clears hallucinations and mental noise",
            List.of(Items.POTION, Items.GOLD_NUGGET, Items.FERMENTED_SPIDER_EYE));

        add(ModItems.get(DisabilityType.MUTE), "Muteness", "Restores voice and speech in chat",
            List.of(Items.POTION, Items.NOTE_BLOCK, Items.FEATHER));

        add(ModItems.get(DisabilityType.SEIZURES), "Seizures", "Stabilizes nervous reflexes and convulsions",
            List.of(Items.POTION, Items.HONEY_BOTTLE, Items.GLOW_BERRIES));

        add(ModItems.get(DisabilityType.CANCER), "Cancer", "Halts max health deterioration",
            List.of(Items.POTION, Items.GLISTERING_MELON_SLICE, Items.GHAST_TEAR));

        add(ModItems.get(DisabilityType.OCD), "OCD", "Relieves compulsive urges",
            List.of(Items.POTION, Items.CLOCK, Items.COMPASS));

        add(ModItems.get(DisabilityType.AMPUTATED_OFFHAND), "Amputated Offhand", "Regenerates the offhand limb",
            List.of(Items.POTION, Items.SHIELD, Items.SLIME_BALL));

        add(ModItems.get(DisabilityType.AMPUTATED_LEG), "Amputated Leg", "Restores natural walking and movement",
            List.of(Items.POTION, Items.LEATHER_BOOTS, Items.SLIME_BALL));

        add(ModItems.get(DisabilityType.DYSLEXIA), "Dyslexia", "Clarifies scrambled text and symbols",
            List.of(Items.POTION, Items.BOOK, Items.GLOW_INK_SAC));

        add(ModItems.get(DisabilityType.SHORT), "Short Stature", "Restores standard player height and reach",
            List.of(Items.POTION, Items.BONE_MEAL, Items.RED_MUSHROOM));

        add(ModItems.get(DisabilityType.DEMENTIA), "Dementia", "Prevents inventory scrambling and lost memories",
            List.of(Items.POTION, Items.CHEST, Items.BOOK));

        add(ModItems.get(DisabilityType.ASTHMA), "Asthma", "Restores lung capacity and running endurance",
            List.of(Items.POTION, Items.PUFFERFISH, Items.FEATHER));

        add(ModItems.get(DisabilityType.CEREBRAL_PALSY), "Cerebral Palsy", "Smoothes voluntary muscle coordination",
            List.of(Items.POTION, Items.HONEY_BOTTLE, Items.SLIME_BALL));

        add(ModItems.get(DisabilityType.NARCOLEPSY), "Narcolepsy", "Prevents sudden sleep episodes and fatigue",
            List.of(Items.POTION, Items.REDSTONE, Items.COCOA_BEANS));

        add(ModItems.get(DisabilityType.PARKINSONS), "Parkinson's", "Steadies shaky crosshairs and motor control",
            List.of(Items.POTION, Items.SWEET_BERRIES, Items.IRON_INGOT));

        add(ModItems.get(DisabilityType.POLLEN_ALLERGY), "Pollen Allergy", "Removes fatal allergic reaction to pollen",
            List.of(Items.POTION, Items.HONEYCOMB, Items.DANDELION));

        add(ModItems.get(DisabilityType.INSOMNIA), "Insomnia", "Restores the natural ability to sleep",
            List.of(Items.POTION, Items.WOOL.white(), Items.SWEET_BERRIES));

        add(ModItems.get(DisabilityType.ARTHRITIS), "Arthritis", "Eliminates mining and attack fatigue",
            List.of(Items.POTION, Items.MAGMA_CREAM, Items.BLAZE_POWDER));

        add(ModItems.get(DisabilityType.BRITTLE_BONES), "Brittle Bones", "Reinforces skeletal durability against fall damage",
            List.of(Items.POTION, Items.BONE_BLOCK, Items.IRON_INGOT));

        add(ModItems.get(DisabilityType.ADHD), "ADHD", "Prevents involuntary hotbar switching",
            List.of(Items.POTION, Items.COPPER_INGOT, Items.REDSTONE));

        add(ModItems.get(DisabilityType.TOURETTES), "Tourette's", "Calms involuntary vocal tics and sudden jerks",
            List.of(Items.POTION, Items.NOTE_BLOCK, Items.SUGAR));

        add(ModItems.get(DisabilityType.VERTIGO), "Vertigo", "Restores balance and stops world spinning",
            List.of(Items.POTION, Items.FEATHER, Items.COMPASS));

        add(ModItems.get(DisabilityType.PROSOPAGNOSIA), "Prosopagnosia", "Restores facial recognition of all mobs",
            List.of(Items.POTION, Items.CARVED_PUMPKIN, Items.GLOWSTONE_DUST));

        add(ModItems.get(DisabilityType.SCOLIOSIS), "Scoliosis", "Straightens permanently tilted perspective",
            List.of(Items.POTION, Items.BAMBOO, Items.IRON_CHAIN));

        add(ModItems.get(DisabilityType.KLEPTOMANIA), "Kleptomania", "Restores the ability to discard and drop items",
            List.of(Items.POTION, Items.CHEST, Items.GOLD_INGOT));

        add(ModItems.get(DisabilityType.LACTOSE_INTOLERANCE), "Lactose Intolerance", "Prevents fatal reaction to dairy and milk",
            List.of(Items.POTION, Items.MILK_BUCKET, Items.APPLE));

        add(ModItems.get(DisabilityType.ANOSOGNOSIA), "Anosognosia", "Restores awareness of your active conditions",
            List.of(Items.POTION, Items.SPYGLASS, Items.GLOWSTONE_DUST));

        initialized = true;
    }

    private static void add(ItemLike antidote, String condition, String lore, List<Item> ingredients) {
        if (antidote != null) {
            ENTRIES.add(new RecipeEntry(antidote.asItem(), condition, lore, ingredients));
        }
    }

    public static List<RecipeEntry> getEntries() {
        if (!initialized) init();
        return Collections.unmodifiableList(ENTRIES);
    }
}
