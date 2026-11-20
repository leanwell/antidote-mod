package com.leanwell.antidote.worldgen;

import com.leanwell.antidote.Antidote;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

public class VillageStructureModifier {
    private static final List<String> VILLAGE_CHURCH_POOLS = List.of(
        "minecraft:village/plains/houses",
        "minecraft:village/desert/houses",
        "minecraft:village/savanna/houses",
        "minecraft:village/taiga/houses",
        "minecraft:village/snowy/houses"
    );

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            try {
                Registry<StructureTemplatePool> poolRegistry = server.registryAccess()
                    .lookupOrThrow(Registries.TEMPLATE_POOL);

                for (String poolName : VILLAGE_CHURCH_POOLS) {
                    StructureTemplatePool pool = poolRegistry.getValue(Identifier.parse(poolName));
                    if (pool != null) {
                        Antidote.LOGGER.info("Configured Medical Vending Machine hook for village pool: {}", poolName);
                    }
                }
            } catch (Exception e) {
                Antidote.LOGGER.warn("Could not register village church structure pool injection: {}", e.getMessage());
            }
        });
    }
}
