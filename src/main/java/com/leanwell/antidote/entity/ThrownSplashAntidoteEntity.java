package com.leanwell.antidote.entity;

import com.leanwell.antidote.item.SplashAntidoteItem;
import com.leanwell.antidote.item.SplashCorruptAntidoteItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ThrownSplashAntidoteEntity extends ThrowableItemProjectile {
    public ThrownSplashAntidoteEntity(EntityType<? extends ThrownSplashAntidoteEntity> type, Level level) {
        super(type, level);
    }

    public ThrownSplashAntidoteEntity(Level level, LivingEntity owner, ItemStack itemStack) {
        super(ModEntities.SPLASH_ANTIDOTE, owner, level, itemStack);
    }

    public ThrownSplashAntidoteEntity(Level level, double x, double y, double z, ItemStack itemStack) {
        super(ModEntities.SPLASH_ANTIDOTE, x, y, z, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level() instanceof ServerLevel level) {
            ItemStack stack = this.getItem();
            Item item = stack.getItem();

            level.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.SPLASH_POTION_BREAK,
                SoundSource.PLAYERS,
                1.0F,
                level.getRandom().nextFloat() * 0.1F + 0.9F
            );

            AABB aabb = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
            List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, aabb);

            for (ServerPlayer player : players) {
                if (this.distanceToSqr(player) <= 16.0) {
                    if (item instanceof SplashAntidoteItem splashAntidote) {
                        splashAntidote.applySplashCure(player, level);
                    } else if (item instanceof SplashCorruptAntidoteItem splashCorrupt) {
                        splashCorrupt.applySplashCurse(player, level);
                    }
                }
            }

            int color = 0x385dc6;
            if (item instanceof SplashAntidoteItem splashAntidote) {
                color = splashAntidote.getDisabilityType().getColor();
            } else if (item instanceof SplashCorruptAntidoteItem) {
                color = 0x330033;
            }
            level.levelEvent(2002, this.blockPosition(), color);

            this.discard();
        }
    }
}
