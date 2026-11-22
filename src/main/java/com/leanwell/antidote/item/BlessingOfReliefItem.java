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

public class BlessingOfReliefItem extends Item {
    public BlessingOfReliefItem(Item.Properties properties) {
        super(properties
            .stacksTo(16)
            .component(DataComponents.FOOD, new FoodProperties(5, 0.6F, true))
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
                server.getCommands().performPrefixedCommand(source, "disability remove " + player.getScoreboardName() + " random");

                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    25, 0.5, 0.6, 0.5, 0.1);
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.2F);

                player.sendOverlayMessage(
                    Component.literal("Affliction eased by the Blessed Bread!")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                );
            }
        }
        return super.finishUsingItem(itemStack, level, entity);
    }
}
