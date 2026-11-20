package com.leanwell.antidote.network;

import com.leanwell.antidote.config.AntidoteConfigManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ModNetworking {
    public static void registerCommon() {
        PayloadTypeRegistry.clientboundPlay().register(SyncConfigPayload.TYPE, SyncConfigPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(SyncConfigPayload.fromConfig(AntidoteConfigManager.getConfig()));
        });
    }

    public static void broadcastConfig(MinecraftServer server) {
        if (server == null) return;
        SyncConfigPayload payload = SyncConfigPayload.fromConfig(AntidoteConfigManager.getConfig());
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
