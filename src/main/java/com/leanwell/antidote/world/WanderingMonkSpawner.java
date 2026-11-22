package com.leanwell.antidote.world;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.entity.ModEntities;
import com.leanwell.antidote.entity.WanderingMonkEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

public class WanderingMonkSpawner {
    private static final int CHECK_INTERVAL_TICKS = 1200; // Check every 1 minute (2.4s at 500 tick rate)
    private static int elapsedCycleTicks = 0;
    private static float cycleThreshold = -1.0F;

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(WanderingMonkSpawner::onWorldTick);
    }

    private static void onWorldTick(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }

        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }

        RandomSource random = level.getRandom();
        if (cycleThreshold < 0.0F) {
            cycleThreshold = random.nextFloat();
        }

        // Only increment spawn cycle timer when no monk is already present nearby
        boolean monkActiveNearby = false;
        for (ServerPlayer player : players) {
            List<WanderingMonkEntity> nearby = level.getEntitiesOfClass(
                WanderingMonkEntity.class,
                player.getBoundingBox().inflate(128.0)
            );
            if (!nearby.isEmpty()) {
                monkActiveNearby = true;
                break;
            }
        }

        if (monkActiveNearby) {
            return;
        }

        elapsedCycleTicks++;
        if (elapsedCycleTicks % CHECK_INTERVAL_TICKS != 0) {
            return;
        }

        float cumulativeChance = getCumulativeChance(elapsedCycleTicks);
        if (cycleThreshold > cumulativeChance) {
            return;
        }

        // Pick a random online player in the Overworld
        ServerPlayer targetPlayer = players.get(random.nextInt(players.size()));
        if (spawnMonkNear(targetPlayer, false)) {
            // Successfully spawned! Reset timer and pick a new random threshold for the next cycle
            resetCycle(random);
        }
    }

    /**
     * Calculates the cumulative spawn probability based on elapsed time:
     * - 2 minutes (2,400 ticks / 0.1 MC days): 0%
     * - 4 minutes (4,800 ticks / 0.2 MC days): 10%
     * - 6 minutes (7,200 ticks / 0.3 MC days): 25%
     * - 10 minutes (12,000 ticks / 0.5 MC days): 35%
     * - 20 minutes (24,000 ticks / 1.0 MC day): 50%
     * - 30 minutes (36,000 ticks / 1.5 MC days): 75%
     * - 40 minutes (48,000 ticks / 2.0 MC days): 80%
     * - 50 minutes (60,000 ticks / 2.5 MC days): 99%
     * - > 50 minutes: 100% (guaranteed)
     */
    public static float getCumulativeChance(int ticks) {
        float mins = ticks / 1200.0F;
        if (mins < 2.0F) {
            return 0.0F;
        } else if (mins < 4.0F) {
            return 0.10F * ((mins - 2.0F) / 2.0F);
        } else if (mins < 6.0F) {
            return 0.10F + 0.15F * ((mins - 4.0F) / 2.0F);
        } else if (mins < 10.0F) {
            return 0.25F + 0.10F * ((mins - 6.0F) / 4.0F);
        } else if (mins < 20.0F) {
            return 0.35F + 0.15F * ((mins - 10.0F) / 10.0F);
        } else if (mins < 30.0F) {
            return 0.50F + 0.25F * ((mins - 20.0F) / 10.0F);
        } else if (mins < 40.0F) {
            return 0.75F + 0.05F * ((mins - 30.0F) / 10.0F);
        } else if (mins < 50.0F) {
            return 0.80F + 0.19F * ((mins - 40.0F) / 10.0F);
        } else {
            return 1.0F;
        }
    }

    public static void resetCycle(RandomSource random) {
        elapsedCycleTicks = 0;
        cycleThreshold = random != null ? random.nextFloat() : 0.5F;
    }

    public static int getElapsedCycleTicks() {
        return elapsedCycleTicks;
    }

    public static float getCycleThreshold() {
        return cycleThreshold;
    }

    public static boolean spawnMonkNear(ServerPlayer targetPlayer, boolean force) {
        if (!(targetPlayer.level() instanceof ServerLevel level)) {
            return false;
        }
        RandomSource random = level.getRandom();

        if (!force) {
            List<WanderingMonkEntity> nearbyMonks = level.getEntitiesOfClass(
                WanderingMonkEntity.class,
                targetPlayer.getBoundingBox().inflate(96.0)
            );
            if (!nearbyMonks.isEmpty()) {
                return false;
            }
        }

        // Try up to 20 positions around the player
        for (int attempt = 0; attempt < 20; attempt++) {
            double distance = (force ? 3.0 : 12.0) + random.nextDouble() * (force ? 6.0 : 24.0);
            double angle = random.nextDouble() * 2.0 * Math.PI;
            double targetX = targetPlayer.getX() + distance * Math.cos(angle);
            double targetZ = targetPlayer.getZ() + distance * Math.sin(angle);

            BlockPos targetPos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BlockPos.containing(targetX, 0, targetZ)
            );

            // Ground must not be air, and spawn space must be clear air
            if (!level.getBlockState(targetPos.below()).isAir()
                && level.getBlockState(targetPos).isAir()
                && level.getBlockState(targetPos.above()).isAir()) {

                WanderingMonkEntity monk = new WanderingMonkEntity(ModEntities.WANDERING_MONK, level);
                monk.snapTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);

                if (level.addFreshEntity(monk)) {
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        monk.getX(), monk.getY() + 1.0, monk.getZ(),
                        25, 0.4, 0.6, 0.4, 0.1);
                    level.playSound(null, monk.getX(), monk.getY(), monk.getZ(),
                        SoundEvents.BELL_RESONATE, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    Antidote.LOGGER.info("Wandering Monk spawned near {} at [{}, {}, {}]",
                        targetPlayer.getScoreboardName(), targetPos.getX(), targetPos.getY(), targetPos.getZ());
                    return true;
                }
            }
        }
        return false;
    }
}
