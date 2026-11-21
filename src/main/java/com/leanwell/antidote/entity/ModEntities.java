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

    public static void register() {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Antidote.id("splash_antidote"));
        SPLASH_ANTIDOTE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            key,
            EntityType.Builder.<ThrownSplashAntidoteEntity>of(ThrownSplashAntidoteEntity::new, MobCategory.MISC)
                .noLootTable()
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .build(key)
        );
        Antidote.LOGGER.info("Registered Splash Antidote projectile entity.");
    }
}
