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
    public static AntidoteItem GOD_ANTIDOTE;

    private static AntidoteItem registerItem(String name, Function<Item.Properties, AntidoteItem> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Antidote.id(name));
        Item.Properties properties = new Item.Properties().setId(key);
        AntidoteItem item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void register() {
        // Register God Antidote first
        GOD_ANTIDOTE = registerItem("god_antidote", GodAntidoteItem::new);
        ANTIDOTES.put(DisabilityType.GOD_ANTIDOTE, GOD_ANTIDOTE);

        // Register all specific condition antidotes
        for (DisabilityType type : DisabilityType.values()) {
            if (type == DisabilityType.GOD_ANTIDOTE) continue;

            String itemId = type.getId() + "_antidote";
            AntidoteItem item = registerItem(itemId, props -> new AntidoteItem(type, props));
            ANTIDOTES.put(type, item);
        }

        Antidote.LOGGER.info("Registered {} Antidote items with ResourceKeys.", ANTIDOTES.size());
    }

    public static Map<DisabilityType, AntidoteItem> getAll() {
        return Collections.unmodifiableMap(ANTIDOTES);
    }

    public static AntidoteItem get(DisabilityType type) {
        return ANTIDOTES.get(type);
    }
}
