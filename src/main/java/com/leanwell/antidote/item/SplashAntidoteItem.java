package com.leanwell.antidote.item;

import com.leanwell.antidote.entity.ThrownSplashAntidoteEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class SplashAntidoteItem extends Item {
    protected final DisabilityType disabilityType;

    public SplashAntidoteItem(DisabilityType disabilityType, Item.Properties properties) {
        super(properties.stacksTo(16));
        this.disabilityType = disabilityType;
    }

    public DisabilityType getDisabilityType() {
        return disabilityType;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return disabilityType == DisabilityType.GOD_ANTIDOTE || super.isFoil(stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.SPLASH_POTION_THROW,
            SoundSource.PLAYERS,
            0.5F,
            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(
                (lvl, pos, stack) -> new ThrownSplashAntidoteEntity(lvl, player, stack),
                serverLevel,
                itemStack,
                player,
                -20.0F,
                0.5F,
                1.0F
            );
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    public void applySplashCure(ServerPlayer player, ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
            CommandSourceStack source = server.createCommandSourceStack()
                .withSuppressedOutput();

            if (disabilityType == DisabilityType.GOD_ANTIDOTE) {
                server.getCommands().performPrefixedCommand(source, "disability reset");
                server.getCommands().performPrefixedCommand(source,
                    "disability remove " + player.getName().getString() + " all");

                serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    30, 0.5, 0.8, 0.5, 0.2);

                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.9F, 1.0F);

                player.sendOverlayMessage(
                    Component.literal("All disabilities cured & timer reset!")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                );
            } else {
                server.getCommands().performPrefixedCommand(source,
                    "disability remove " + player.getName().getString() + " " + disabilityType.getId());

                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    15, 0.5, 0.5, 0.5, 0.1);

                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.2F);

                player.sendOverlayMessage(
                    Component.literal("Cured " + disabilityType.getDisplayName() + "!")
                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal("Splash Area Cure").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        consumer.accept(Component.literal(disabilityType.getLore()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
    }
}
