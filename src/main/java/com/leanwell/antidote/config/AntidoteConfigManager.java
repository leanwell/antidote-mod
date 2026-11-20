package com.leanwell.antidote.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.store.VendingCatalog;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class AntidoteConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("antidote.json");

    private static AntidoteConfig config = new AntidoteConfig();

    public static AntidoteConfig getConfig() {
        return config;
    }

    public static void setConfig(AntidoteConfig newConfig) {
        if (newConfig != null) {
            config = newConfig;
            VendingCatalog.applyConfig(config);
        }
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                    AntidoteConfig loaded = GSON.fromJson(reader, AntidoteConfig.class);
                    if (loaded != null) {
                        config = loaded;
                        Antidote.LOGGER.info("Loaded Antidote configuration from {}", CONFIG_FILE);
                    } else {
                        save(); // write default
                    }
                }
            } else {
                save(); // write default
            }
        } catch (Exception e) {
            Antidote.LOGGER.error("Failed to load Antidote configuration: {}", e.getMessage(), e);
        }

        VendingCatalog.applyConfig(config);
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
                Antidote.LOGGER.info("Saved default Antidote configuration to {}", CONFIG_FILE);
            }
        } catch (Exception e) {
            Antidote.LOGGER.error("Failed to save Antidote configuration: {}", e.getMessage(), e);
        }
    }

    public static void reload() {
        load();
    }
}
