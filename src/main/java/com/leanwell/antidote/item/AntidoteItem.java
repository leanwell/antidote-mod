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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class AntidoteItem extends Item {
    protected final DisabilityType disabilityType;

    public AntidoteItem(DisabilityType disabilityType, Item.Properties properties) {
        super(properties
            .stacksTo(16)
            .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
            .usingConvertsTo(Items.GLASS_BOTTLE));
        this.disabilityType = disabilityType;
    }

    public DisabilityType getDisabilityType() {
        return disabilityType;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            applyCure(player, (ServerLevel) level);
        }
        return super.finishUsingItem(itemStack, level, entity);
    }

    protected void applyCure(ServerPlayer player, ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
            CommandSourceStack source = server.createCommandSourceStack()
                .withSuppressedOutput();

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

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.literal(disabilityType.getLore()).withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("Drink to cure " + disabilityType.getDisplayName()).withStyle(ChatFormatting.DARK_GREEN));
    }
}
