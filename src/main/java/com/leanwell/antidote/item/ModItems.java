package com.leanwell.antidote.item;

import com.leanwell.antidote.Antidote;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    private static final Map<DisabilityType, AntidoteItem> ANTIDOTES = new LinkedHashMap<>();
    public static AntidoteItem GOD_ANTIDOTE;

    public static void register() {
        // Register God Antidote first
        GOD_ANTIDOTE = Registry.register(
            BuiltInRegistries.ITEM,
            Antidote.id("god_antidote"),
            new GodAntidoteItem(new Item.Properties())
        );
        ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, GOD_ANTIDOTE);

        // Register all specific condition antidotes
        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;

            String itemId = type.getId() + "_antidote";
            AntidoteItem item = Registry.register(
                BuiltInRegistries.ITEM,
                Antidote.id(itemId),
                new AntidoteItem(type, new Item.Properties())
            );
            ANTIDOTES.put(type, item);
        }

        Antidote.LOGGER.info("Registered {} Antidote items.", ANTIDOTES.size());
    }

    public static Map<DisabilityType, AntidoteItem> getAll() {
        return Collections.unmodifiableMap(ANTIDOTES);
    }

    public static AntidoteItem get(DisabilityType type) {
        return ANTIDOTES.get(type);
    }
}
