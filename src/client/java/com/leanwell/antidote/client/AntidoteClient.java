package com.leanwell.antidote.client;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.client.gui.MedicalVendingScreen;
import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.network.SyncConfigPayload;
import com.leanwell.antidote.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;

public class AntidoteClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModScreenHandlers.MEDICAL_VENDING, MedicalVendingScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(SyncConfigPayload.TYPE, (payload, context) -> {
			context.client().execute(() -> {
				AntidoteConfigManager.setConfig(payload.toConfig());
				Antidote.LOGGER.info("Synchronized pricing configuration from server.");
			});
		});

		Antidote.LOGGER.info("Antidote client initialized with Medical Vending Screen and Network Receivers.");
	}
}
