package com.leanwell.antidote.entity;

import com.leanwell.antidote.Antidote;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static EntityType<ThrownSplashAntidoteEntity> SPLASH_ANTIDOTE;
    public static EntityType<WanderingMonkEntity> WANDERING_MONK;

    public static void register() {
        ResourceKey<EntityType<?>> splashKey = ResourceKey.create(Registries.ENTITY_TYPE, Antidote.id("splash_antidote"));
        SPLASH_ANTIDOTE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            splashKey,
            EntityType.Builder.<ThrownSplashAntidoteEntity>of(ThrownSplashAntidoteEntity::new, MobCategory.MISC)
                .noLootTable()
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .build(splashKey)
        );

        ResourceKey<EntityType<?>> monkKey = ResourceKey.create(Registries.ENTITY_TYPE, Antidote.id("wandering_monk"));
        WANDERING_MONK = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            monkKey,
            EntityType.Builder.<WanderingMonkEntity>of(WanderingMonkEntity::new, MobCategory.CREATURE)
                .sized(0.6F, 1.95F)
                .clientTrackingRange(10)
                .build(monkKey)
        );

        Antidote.LOGGER.info("Registered Antidote entities (Splash Antidote, Wandering Monk).");
    }
}
