package com.leanwell.antidote;

import com.leanwell.antidote.block.ModBlocks;
import com.leanwell.antidote.block.entity.ModBlockEntities;
import com.leanwell.antidote.command.AntidoteCommand;
import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.item.ModItemGroups;
import com.leanwell.antidote.item.ModItems;
import com.leanwell.antidote.network.ModNetworking;
import com.leanwell.antidote.screen.ModScreenHandlers;
import com.leanwell.antidote.worldgen.VillageStructureModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Antidote implements ModInitializer {
	public static final String MOD_ID = "antidote";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Antidote Mod v1.1.2 for Minecraft 26.3...");
		AntidoteConfigManager.load();
		ModBlocks.register();
		ModBlockEntities.register();
		ModScreenHandlers.register();
		ModItems.register();
		ModItemGroups.register();
		VillageStructureModifier.register();
		ModNetworking.registerCommon();
		AntidoteCommand.register();
		LOGGER.info("Antidote Mod initialized successfully.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
