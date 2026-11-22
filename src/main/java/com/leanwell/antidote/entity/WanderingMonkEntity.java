package com.leanwell.antidote.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class WanderingMonkEntity extends WanderingTrader {
    private static final int DEFAULT_LIFESPAN_TICKS = 24000; // 20 minutes standing time

    public WanderingMonkEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
        this.setDespawnDelay(DEFAULT_LIFESPAN_TICKS);
    }

    public static AttributeSupplier.Builder createMonkAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.MOVEMENT_SPEED, 0.45);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Raider.class, 12.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 0.45));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.4));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    protected void updateTrades(ServerLevel level) {
        MerchantOffers offers = this.getOffers();
        offers.clear();

        // Trade 1: Minor Cleanse (5 Bread -> Blessing of Relief)
        ItemStack minorBlessing = new ItemStack(Items.PAPER);
        minorBlessing.set(DataComponents.CUSTOM_NAME, Component.literal("Blessing of Relief").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        offers.add(new MerchantOffer(
            new ItemCost(Items.BREAD, 5),
            Optional.empty(),
            minorBlessing,
            3, // Max 3 minor cures per monk visit
            0,
            0.0F
        ));

        // Trade 2: Grand Cleanse (8 Bread + 1 Honey Bottle -> Grand Enlightenment)
        ItemStack grandBlessing = new ItemStack(Items.TOTEM_OF_UNDYING);
        grandBlessing.set(DataComponents.CUSTOM_NAME, Component.literal("Grand Enlightenment").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
        offers.add(new MerchantOffer(
            new ItemCost(Items.BREAD, 8),
            Optional.of(new ItemCost(Items.HONEY_BOTTLE, 1)),
            grandBlessing,
            1, // Exactly 1 grand cure per monk visit
            0,
            0.0F
        ));
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (this.level() instanceof ServerLevel serverLevel && this.getTradingPlayer() instanceof ServerPlayer player) {
            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
                boolean isGrand = offer.getItemCostB().isPresent()
                    && offer.getItemCostB().get().item().value() == Items.HONEY_BOTTLE;

                if (isGrand) {
                    // Grand Cleanse: cure all 30 conditions and reset timer
                    server.getCommands().performPrefixedCommand(source, "disability reset");
                    server.getCommands().performPrefixedCommand(source, "disability remove " + player.getName().getString() + " all");

                    // Return empty glass bottle for the consumed honey bottle
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    if (!player.getInventory().add(emptyBottle)) {
                        player.spawnAtLocation(serverLevel, emptyBottle);
                    }

                    serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        40, 0.5, 0.8, 0.5, 0.2);
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.0F);

                    player.sendOverlayMessage(
                        Component.literal("All afflictions cleansed by the Monk's Grand Blessing!")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                    );

                    // Once the grand cleansing is granted, the monk peacefully departs
                    depart();
                } else {
                    // Minor Cleanse: cure 1 condition
                    server.getCommands().performPrefixedCommand(source, "disability remove " + player.getName().getString() + " random");

                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        20, 0.5, 0.6, 0.5, 0.1);
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.2F);

                    player.sendOverlayMessage(
                        Component.literal("The Monk has blessed you and eased an affliction.")
                            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
                    );

                    // If all minor cures are used up, the monk departs
                    if (offer.getUses() >= offer.getMaxUses()) {
                        depart();
                    }
                }
            }
        }
    }

    public void depart() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                this.getX(), this.getY() + 1.0, this.getZ(),
                30, 0.4, 0.6, 0.4, 0.05);
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                this.getX(), this.getY() + 1.0, this.getZ(),
                20, 0.3, 0.5, 0.3, 0.1);
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BELL_RESONATE, SoundSource.NEUTRAL, 0.9F, 1.4F);
            this.discard();
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            attacker.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
            attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
        }

        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
            this.getX(), this.getY() + 1.0, this.getZ(),
            35, 0.5, 0.6, 0.5, 0.08);
        serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.2F);

        teleportSafely(serverLevel, 16);
        return super.hurtServer(serverLevel, source, amount);
    }

    private void teleportSafely(ServerLevel level, int radius) {
        for (int i = 0; i < 16; ++i) {
            double targetX = this.getX() + (this.random.nextDouble() - 0.5) * 2.0 * radius;
            double targetZ = this.getZ() + (this.random.nextDouble() - 0.5) * 2.0 * radius;
            BlockPos topPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockPos.containing(targetX, 0, targetZ));
            if (level.getBlockState(topPos.below()).isSolid()) {
                this.teleportTo(topPos.getX() + 0.5, topPos.getY(), topPos.getZ() + 0.5);
                break;
            }
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null; // Monks do not breed
    }
}
