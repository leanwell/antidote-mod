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

public class SplashCorruptAntidoteItem extends Item {
    protected final DisabilityType disabilityType;

    public SplashCorruptAntidoteItem(DisabilityType disabilityType, Item.Properties properties) {
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

    public void applySplashCurse(ServerPlayer player, ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
            CommandSourceStack source = server.createCommandSourceStack()
                .withSuppressedOutput();

            if (disabilityType == DisabilityType.GOD_ANTIDOTE) {
                server.getCommands().performPrefixedCommand(source,
                    "disability give " + player.getName().getString() + " all");

                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    35, 0.5, 0.8, 0.5, 0.15);

                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_DEATH, SoundSource.PLAYERS, 0.9F, 0.8F);

                player.sendOverlayMessage(
                    Component.literal("Afflicted with ALL disabilities!")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD)
                );
            } else {
                server.getCommands().performPrefixedCommand(source,
                    "disability give " + player.getName().getString() + " " + disabilityType.getId());

                serverLevel.sendParticles(ParticleTypes.WITCH,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    20, 0.5, 0.5, 0.5, 0.1);

                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.6F, 1.4F);

                player.sendOverlayMessage(
                    Component.literal("Afflicted with " + disabilityType.getDisplayName() + "!")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD)
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal("Splash Area Curse").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        if (disabilityType == DisabilityType.GOD_ANTIDOTE) {
            consumer.accept(Component.literal("Inflicts all 30 conditions in the splash zone!").withStyle(ChatFormatting.DARK_RED));
        } else {
            consumer.accept(Component.literal("Inflicts " + disabilityType.getDisplayName() + " on targets in splash zone").withStyle(ChatFormatting.DARK_RED));
        }
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
    }
}
