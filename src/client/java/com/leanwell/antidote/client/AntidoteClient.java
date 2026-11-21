package com.leanwell.antidote.client;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.client.gui.MedicalVendingScreen;
import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.entity.ModEntities;
import com.leanwell.antidote.network.SyncConfigPayload;
import com.leanwell.antidote.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class AntidoteClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModScreenHandlers.MEDICAL_VENDING, MedicalVendingScreen::new);
		EntityRendererRegistry.register(ModEntities.SPLASH_ANTIDOTE, ThrownItemRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(SyncConfigPayload.TYPE, (payload, context) -> {
			context.client().execute(() -> {
				AntidoteConfigManager.setConfig(payload.toConfig());
				Antidote.LOGGER.info("Synchronized pricing configuration from server.");
			});
		});

		Antidote.LOGGER.info("Antidote client initialized with Medical Vending Screen and Network Receivers.");
	}
}
