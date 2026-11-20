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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class GodAntidoteItem extends AntidoteItem {
    public GodAntidoteItem(Item.Properties properties) {
        super(DisabilityType.GOD_ANTIDOTE, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    protected void applyCure(ServerPlayer player, ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
            CommandSourceStack source = server.createCommandSourceStack()
                .withSuppressedOutput();

            server.getCommands().performPrefixedCommand(source, "disability reset");
            server.getCommands().performPrefixedCommand(source,
                "disability remove " + player.getName().getString() + " all");

            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                player.getX(), player.getY() + 1.0, player.getZ(),
                40, 0.5, 0.8, 0.5, 0.2);

            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

            player.sendOverlayMessage(
                Component.literal("All disabilities cured & timer reset!")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
            );
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.literal("Clears all active disabilities and resets the timer.")
            .withStyle(ChatFormatting.GOLD));
        builder.accept(Component.literal("Legendary Cure-All").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
    }
}
