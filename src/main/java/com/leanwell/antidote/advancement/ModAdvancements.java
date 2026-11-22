package com.leanwell.antidote.advancement;

import com.leanwell.antidote.Antidote;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ModAdvancements {
    public static void award(ServerPlayer player, Identifier id) {
        if (player == null || player.level() == null) {
            return;
        }
        MinecraftServer server = player.level().getServer();
        if (server == null) {
            return;
        }
        AdvancementHolder holder = server.getAdvancements().get(id);
        if (holder != null) {
            player.getAdvancements().award(holder, "impossible");
        }
    }

    public static void award(ServerPlayer player, String path) {
        award(player, Antidote.id(path));
    }
}
