package com.leanwell.antidote.item;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

public class GrandEnlightenmentItem extends Item {
    public GrandEnlightenmentItem(Item.Properties properties) {
        super(properties
            .stacksTo(16)
            .component(DataComponents.FOOD, new FoodProperties(8, 1.2F, true))
            .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD)
        );
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Enchanted glint effect
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            ServerLevel serverLevel = (ServerLevel) level;
            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
                server.getCommands().performPrefixedCommand(source, "disability reset");
                server.getCommands().performPrefixedCommand(source, "disability remove " + player.getScoreboardName() + " all");

                serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    50, 0.5, 0.8, 0.5, 0.2);
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.8F, 1.0F);

                player.sendOverlayMessage(
                    Component.literal("All afflictions cleansed by Grand Enlightenment!")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                );

                com.leanwell.antidote.advancement.ModAdvancements.award(player, "enlightenment");
            }
        }
        return super.finishUsingItem(itemStack, level, entity);
    }
}
