package com.leanwell.antidote.command;

import com.leanwell.antidote.config.AntidoteConfig;
import com.leanwell.antidote.config.AntidoteConfigManager;
import com.leanwell.antidote.network.ModNetworking;
import com.leanwell.antidote.store.VendingCatalog;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

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
                .then(Commands.literal("price")
                    .then(Commands.literal("antidotes")
                        .then(Commands.argument("emeraldCost", IntegerArgumentType.integer(1))
                            .executes(context -> {
                                int cost = IntegerArgumentType.getInteger(context, "emeraldCost");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.antidotes.baseEmeraldCost = cost;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set standard antidotes base cost to " + cost + " Emeralds."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("ingredients")
                        .then(Commands.argument("emeraldCost", IntegerArgumentType.integer(1))
                            .executes(context -> {
                                int cost = IntegerArgumentType.getInteger(context, "emeraldCost");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.ingredients.baseEmeraldCost = cost;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set ingredients base cost to " + cost + " Emeralds."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("god_antidote")
                        .then(Commands.argument("emeraldCost", IntegerArgumentType.integer(1))
                            .executes(context -> {
                                int cost = IntegerArgumentType.getInteger(context, "emeraldCost");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.godAntidote.baseEmeraldCost = cost;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set God Antidote base cost to " + cost + " Emeralds (Diamonds: " + cfg.godAntidote.baseDiamondCost + "D)."), true);
                                return 1;
                            })
                            .then(Commands.argument("diamondCost", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int emeralds = IntegerArgumentType.getInteger(context, "emeraldCost");
                                    int diamonds = IntegerArgumentType.getInteger(context, "diamondCost");
                                    AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                    cfg.godAntidote.baseEmeraldCost = emeralds;
                                    cfg.godAntidote.baseDiamondCost = diamonds;
                                    saveAndSync(context.getSource().getServer());
                                    context.getSource().sendSuccess(() -> Component.literal("Set God Antidote base cost to " + emeralds + " Emeralds and " + diamonds + " Diamonds."), true);
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("multiplier")
                    .then(Commands.literal("antidotes")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01))
                            .executes(context -> {
                                double val = DoubleArgumentType.getDouble(context, "value");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.antidotes.priceMultiplier = val;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set standard antidotes price multiplier to " + val + " (" + Math.round(val * 100) + "%)."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("ingredients")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01))
                            .executes(context -> {
                                double val = DoubleArgumentType.getDouble(context, "value");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.ingredients.priceMultiplier = val;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set ingredients price multiplier to " + val + " (" + Math.round(val * 100) + "%)."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("god_antidote")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01))
                            .executes(context -> {
                                double val = DoubleArgumentType.getDouble(context, "value");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.godAntidote.priceMultiplier = val;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set God Antidote price multiplier to " + val + " (" + Math.round(val * 100) + "%)."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("god_tier")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01))
                            .executes(context -> {
                                double val = DoubleArgumentType.getDouble(context, "value");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.godTierItems.priceMultiplier = val;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set other God Tier items price multiplier to " + val + " (" + Math.round(val * 100) + "%)."), true);
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("all")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01))
                            .executes(context -> {
                                double val = DoubleArgumentType.getDouble(context, "value");
                                AntidoteConfig cfg = AntidoteConfigManager.getConfig();
                                cfg.antidotes.priceMultiplier = val;
                                cfg.godAntidote.priceMultiplier = val;
                                cfg.ingredients.priceMultiplier = val;
                                cfg.godTierItems.priceMultiplier = val;
                                saveAndSync(context.getSource().getServer());
                                context.getSource().sendSuccess(() -> Component.literal("Set all store price multipliers to " + val + " (" + Math.round(val * 100) + "%)."), true);
                                return 1;
                            })
                        )
                    )
                )
                .then(Commands.literal("monk")
                    .then(Commands.literal("spawn")
                        .executes(context -> {
                            if (context.getSource().getPlayer() != null) {
                                boolean spawned = com.leanwell.antidote.world.WanderingMonkSpawner.spawnMonkNear(context.getSource().getPlayer(), true);
                                if (spawned) {
                                    context.getSource().sendSuccess(() -> Component.literal("Wandering Monk summoned nearby!"), true);
                                    return 1;
                                } else {
                                    context.getSource().sendFailure(Component.literal("Could not find a suitable surface ground position nearby."));
                                    return 0;
                                }
                            }
                            context.getSource().sendFailure(Component.literal("This command must be run by a player."));
                            return 0;
                        })
                    )
                )
            );
        });
    }

    private static void saveAndSync(MinecraftServer server) {
        AntidoteConfigManager.save();
        VendingCatalog.applyConfig(AntidoteConfigManager.getConfig());
        ModNetworking.broadcastConfig(server);
    }
}
