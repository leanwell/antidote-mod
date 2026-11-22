package com.leanwell.antidote.item;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GrandEnlightenmentItem extends Item {
    public GrandEnlightenmentItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
                server.getCommands().performPrefixedCommand(source, "disability reset");
                server.getCommands().performPrefixedCommand(source, "disability remove " + serverPlayer.getName().getString() + " all");

                serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    serverPlayer.getX(), serverPlayer.getY() + 1.0, serverPlayer.getZ(),
                    50, 0.5, 0.8, 0.5, 0.2);
                serverLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                    SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.8F, 1.0F);

                serverPlayer.sendOverlayMessage(
                    Component.literal("All afflictions cleansed by Grand Enlightenment!")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                );
            }
        }
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
