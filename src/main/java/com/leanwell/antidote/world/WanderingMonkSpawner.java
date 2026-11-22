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
    private static final int CHECK_INTERVAL_TICKS = 24000; // 20 minutes (1 Minecraft Day)
    private static final float SPAWN_CHANCE = 0.25F; // 25% chance per day
    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(WanderingMonkSpawner::onWorldTick);
    }

    private static void onWorldTick(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL_TICKS) {
            return;
        }
        tickCounter = 0;

        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }

        RandomSource random = level.getRandom();
        if (random.nextFloat() > SPAWN_CHANCE) {
            Antidote.LOGGER.debug("Wandering Monk daily spawn check failed roll.");
            return;
        }

        // Pick a random online player in the Overworld
        ServerPlayer targetPlayer = players.get(random.nextInt(players.size()));
        spawnMonkNear(targetPlayer, false);
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
                Antidote.LOGGER.debug("Wandering Monk already present near player {}", targetPlayer.getName().getString());
                return false;
            }
        }

        // Try up to 10 positions around the player (between 12 and 36 blocks away)
        for (int attempt = 0; attempt < 16; attempt++) {
            double distance = (force ? 6.0 : 16.0) + random.nextDouble() * 20.0;
            double angle = random.nextDouble() * 2.0 * Math.PI;
            double targetX = targetPlayer.getX() + distance * Math.cos(angle);
            double targetZ = targetPlayer.getZ() + distance * Math.sin(angle);

            BlockPos targetPos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BlockPos.containing(targetX, 0, targetZ)
            );

            if (level.getBlockState(targetPos.below()).isSolid()
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
                        targetPlayer.getName().getString(), targetPos.getX(), targetPos.getY(), targetPos.getZ());
                    return true;
                }
            }
        }
        return false;
    }
}
