package com.leanwell.antidote.mixin;

import com.leanwell.antidote.worldgen.VillageStructureModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoolElementStructurePiece.class)
public abstract class PoolElementStructurePieceMixin {
    @Shadow @Final protected StructurePoolElement element;
    @Shadow protected BlockPos position;
    @Shadow @Final protected Rotation rotation;

    @Inject(method = "place", at = @At("TAIL"))
    private void onPiecePlaced(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator,
                               RandomSource random, BoundingBox boundingBox, BlockPos pos, boolean keepJigsaws, CallbackInfo ci) {
        if (this.element instanceof SinglePoolElement singlePoolElement) {
            Identifier templateLocation = singlePoolElement.getTemplateLocation();
            if (templateLocation != null) {
                String path = templateLocation.getPath();
                if (path.contains("village") && path.contains("temple")) {
                    VillageStructureModifier.tryGenerateVendingMachine(world, boundingBox, this.position, this.rotation, templateLocation);
                }
            }
        }
    }
}
