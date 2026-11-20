package com.leanwell.antidote;

import com.leanwell.antidote.item.ModItemGroups;
import com.leanwell.antidote.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Antidote implements ModInitializer {
	public static final String MOD_ID = "antidote";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Antidote Mod for Minecraft 26.3...");
		ModItems.register();
		ModItemGroups.register();
		LOGGER.info("Antidote Mod initialized successfully.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
