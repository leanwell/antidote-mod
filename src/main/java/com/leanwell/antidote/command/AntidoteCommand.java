package com.leanwell.antidote.command;

import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.network.ModNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AntidoteCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("antidote")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("reload")
                    .executes(context -> {
                        AntidoteConfigManager.reload();
                        ModNetworking.broadcastConfig(context.getSource().getServer());
                        context.getSource().sendSuccess(() -> Component.literal("Antidote configuration reloaded and synchronized successfully."), true);
                        return 1;
                    })
                )
            );
        });
    }
}
