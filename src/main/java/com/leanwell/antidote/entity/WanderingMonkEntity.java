package com.leanwell.antidote.entity;

import com.leanwell.antidote.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
    private boolean shouldDepartAfterTrade = false;
    private int departureTimer = -1;

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
        offers.add(new MerchantOffer(
            new ItemCost(Items.BREAD, 5),
            Optional.empty(),
            new ItemStack(ModItems.BLESSING_OF_RELIEF),
            3, // Max 3 minor cures per monk visit
            1, // Reward 1 xp so notifyTrade triggers reliably
            0.0F
        ));

        // Trade 2: Grand Cleanse (20 Bread + 2 Honey Bottles -> Fruit of Enlightenment)
        offers.add(new MerchantOffer(
            new ItemCost(Items.BREAD, 20),
            Optional.of(new ItemCost(Items.HONEY_BOTTLE, 2)),
            new ItemStack(ModItems.GRAND_ENLIGHTENMENT),
            1, // Exactly 1 grand cure per monk visit
            1, // Reward 1 xp
            0.0F
        ));
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        super.notifyTrade(offer);

        boolean isGrand = offer.getItemCostB().isPresent()
            && offer.getItemCostB().get().item().value() == Items.HONEY_BOTTLE;

        if (this.level() instanceof ServerLevel serverLevel && this.getTradingPlayer() instanceof ServerPlayer player) {
            if (isGrand) {
                // Refund 2 empty glass bottles for the 2 consumed honey bottles
                ItemStack emptyBottles = new ItemStack(Items.GLASS_BOTTLE, 2);
                if (!player.getInventory().add(emptyBottles)) {
                    player.spawnAtLocation(serverLevel, emptyBottles);
                }
                this.shouldDepartAfterTrade = true;
                this.departureTimer = 100; // Depart in 5s if player stays in menu
            } else if (offer.getUses() >= offer.getMaxUses()) {
                this.shouldDepartAfterTrade = true;
                this.departureTimer = 100;
            }
        }
    }

    @Override
    public void setTradingPlayer(Player player) {
        super.setTradingPlayer(player);
        if (player == null && this.shouldDepartAfterTrade) {
            depart();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.departureTimer > 0) {
            this.departureTimer--;
            if (this.departureTimer == 0) {
                depart();
            }
        }
    }

    public void depart() {
        if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                this.getX(), this.getY() + 1.0, this.getZ(),
                35, 0.4, 0.6, 0.4, 0.05);
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                this.getX(), this.getY() + 1.0, this.getZ(),
                25, 0.3, 0.5, 0.3, 0.1);
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BELL_RESONATE, SoundSource.NEUTRAL, 1.0F, 1.3F);
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
