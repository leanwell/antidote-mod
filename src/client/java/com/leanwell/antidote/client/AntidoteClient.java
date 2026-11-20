package com.leanwell.antidote.client;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.client.gui.MedicalVendingScreen;
import com.leanwell.antidote.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class AntidoteClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModScreenHandlers.MEDICAL_VENDING, MedicalVendingScreen::new);
		Antidote.LOGGER.info("Antidote client initialized with Medical Vending Screen.");
	}
}
