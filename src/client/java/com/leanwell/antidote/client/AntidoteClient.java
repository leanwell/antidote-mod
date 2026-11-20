package com.leanwell.antidote.client;

import com.leanwell.antidote.Antidote;
import net.fabricmc.api.ClientModInitializer;

public class AntidoteClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Antidote.LOGGER.info("Antidote client initialized.");
	}
}
