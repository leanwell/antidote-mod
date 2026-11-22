package com.leanwell.antidote.item;

import com.leanwell.antidote.Antidote;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ModItems {
    private static final Map<DisabilityType, AntidoteItem> ANTIDOTES = new LinkedHashMap<>();
    private static final Map<DisabilityType, SplashAntidoteItem> SPLASH_ANTIDOTES = new LinkedHashMap<>();
    private static final Map<DisabilityType, CorruptAntidoteItem> CORRUPT_ANTIDOTES = new LinkedHashMap<>();
    private static final Map<DisabilityType, SplashCorruptAntidoteItem> SPLASH_CORRUPT_ANTIDOTES = new LinkedHashMap<>();

    public static AntidoteItem GOD_ANTIDOTE;
    public static SplashAntidoteItem SPLASH_GOD_ANTIDOTE;
    public static CorruptAntidoteItem CORRUPT_GOD_ANTIDOTE;
    public static SplashCorruptAntidoteItem SPLASH_CORRUPT_GOD_ANTIDOTE;
    public static BlessingOfReliefItem BLESSING_OF_RELIEF;
    public static GrandEnlightenmentItem GRAND_ENLIGHTENMENT;

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Antidote.id(name));
        Item.Properties properties = new Item.Properties().setId(key);
        T item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void register() {
        // 1. Standard Antidotes
        GOD_ANTIDOTE = registerItem("god_antidote", GodAntidoteItem::new);
        ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, GOD_ANTIDOTE);

        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;
            String itemId = type.getId() + "_antidote";
            AntidoteItem item = registerItem(itemId, props -> new AntidoteItem(type, props));
            ANTIDOTES.put(type, item);
        }

        // 2. Splash Antidotes
        SPLASH_GOD_ANTIDOTE = registerItem("splash_god_antidote", props -> new SplashAntidoteItem(DisabilityType.GOD_ANTIDOTE, props));
        SPLASH_ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, SPLASH_GOD_ANTIDOTE);

        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;
            String itemId = "splash_" + type.getId() + "_antidote";
            SplashAntidoteItem item = registerItem(itemId, props -> new SplashAntidoteItem(type, props));
            SPLASH_ANTIDOTES.put(type, item);
        }

        // 3. Corrupt Antidotes (Drinkable Curse)
        CORRUPT_GOD_ANTIDOTE = registerItem("corrupt_god_antidote", props -> new CorruptAntidoteItem(DisabilityType.GOD_ANTIDOTE, props));
        CORRUPT_ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, CORRUPT_GOD_ANTIDOTE);

        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;
            String itemId = "corrupt_" + type.getId() + "_antidote";
            CorruptAntidoteItem item = registerItem(itemId, props -> new CorruptAntidoteItem(type, props));
            CORRUPT_ANTIDOTES.put(type, item);
        }

        // 4. Splash Corrupt Antidotes (Splash Curse)
        SPLASH_CORRUPT_GOD_ANTIDOTE = registerItem("splash_corrupt_god_antidote", props -> new SplashCorruptAntidoteItem(DisabilityType.GOD_ANTIDOTE, props));
        SPLASH_CORRUPT_ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, SPLASH_CORRUPT_GOD_ANTIDOTE);

        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;
            String itemId = "splash_corrupt_" + type.getId() + "_antidote";
            SplashCorruptAntidoteItem item = registerItem(itemId, props -> new SplashCorruptAntidoteItem(type, props));
            SPLASH_CORRUPT_ANTIDOTES.put(type, item);
        }

        // 5. Wandering Monk Sacred Offerings
        BLESSING_OF_RELIEF = registerItem("blessing_of_relief", BlessingOfReliefItem::new);
        GRAND_ENLIGHTENMENT = registerItem("grand_enlightenment", GrandEnlightenmentItem::new);

        Antidote.LOGGER.info("Registered {} Antidotes, {} Splash Antidotes, {} Corrupt Potions, and {} Splash Corrupt Potions.",
            ANTIDOTES.size(), SPLASH_ANTIDOTES.size(), CORRUPT_ANTIDOTES.size(), SPLASH_CORRUPT_ANTIDOTES.size());
    }

    public static Map<DisabilityType, AntidoteItem> getAll() {
        return Collections.unmodifiableMap(ANTIDOTES);
    }

    public static AntidoteItem get(DisabilityType type) {
        return ANTIDOTES.get(type);
    }

    public static Map<DisabilityType, SplashAntidoteItem> getAllSplash() {
        return Collections.unmodifiableMap(SPLASH_ANTIDOTES);
    }

    public static SplashAntidoteItem getSplash(DisabilityType type) {
        return SPLASH_ANTIDOTES.get(type);
    }

    public static Map<DisabilityType, CorruptAntidoteItem> getAllCorrupt() {
        return Collections.unmodifiableMap(CORRUPT_ANTIDOTES);
    }

    public static CorruptAntidoteItem getCorrupt(DisabilityType type) {
        return CORRUPT_ANTIDOTES.get(type);
    }

    public static Map<DisabilityType, SplashCorruptAntidoteItem> getAllSplashCorrupt() {
        return Collections.unmodifiableMap(SPLASH_CORRUPT_ANTIDOTES);
    }

    public static SplashCorruptAntidoteItem getSplashCorrupt(DisabilityType type) {
        return SPLASH_CORRUPT_ANTIDOTES.get(type);
    }
}
