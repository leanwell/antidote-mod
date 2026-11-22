package com.leanwell.antidote;

import com.leanwell.antidote.block.ModBlocks;
import com.leanwell.antidote.block.entity.ModBlockEntities;
import com.leanwell.antidote.command.AntidoteCommand;
import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.entity.ModEntities;
import com.leanwell.antidote.entity.WanderingMonkEntity;
import com.leanwell.antidote.item.ModItemGroups;
import com.leanwell.antidote.item.ModItems;
import com.leanwell.antidote.network.ModNetworking;
import com.leanwell.antidote.screen.ModScreenHandlers;
import com.leanwell.antidote.world.WanderingMonkSpawner;
import com.leanwell.antidote.worldgen.VillageStructureModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Antidote implements ModInitializer {
	public static final String MOD_ID = "antidote";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Antidote Mod v1.3.0 for Minecraft 26.3...");
		ModBlocks.register();
		ModBlockEntities.register();
		ModScreenHandlers.register();
		ModEntities.register();
		FabricDefaultAttributeRegistry.register(ModEntities.WANDERING_MONK, WanderingMonkEntity.createMonkAttributes());
		ModItems.register();
		ModItemGroups.register();
		VillageStructureModifier.register();
		WanderingMonkSpawner.register();
		AntidoteConfigManager.load();
		ModNetworking.registerCommon();
		AntidoteCommand.register();
		LOGGER.info("Antidote Mod initialized successfully.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
