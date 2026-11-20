package com.leanwell.antidote.worldgen;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.block.MedicalVendingMachineBlock;
import com.leanwell.antidote.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class VillageStructureModifier {

    public static void register() {
        Antidote.LOGGER.info("Registered VillageStructureModifier for village church injections.");
    }

    public static void tryGenerateVendingMachine(WorldGenLevel world, BoundingBox boundingBox, BlockPos piecePos,
                                                Rotation rotation, Identifier templateLocation) {
        // 1. Check if a vending machine was already generated nearby for this church
        for (int dx = -8; dx <= 8; dx += 2) {
            for (int dz = -8; dz <= 8; dz += 2) {
                for (int dy = -2; dy <= 4; dy++) {
                    BlockPos checkPos = piecePos.offset(dx, dy, dz);
                    if (world.getBlockState(checkPos).is(ModBlocks.MEDICAL_VENDING_MACHINE)) {
                        return; // Already generated for this church!
                    }
                }
            }
        }

        // 2. Find the Brewing Stand inside the church bounding box
        BlockPos brewingStandPos = null;
        for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
            for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
                for (int y = boundingBox.minY(); y <= boundingBox.maxY(); y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    if (world.getBlockState(p).is(Blocks.BREWING_STAND)) {
                        brewingStandPos = p;
                        break;
                    }
                }
                if (brewingStandPos != null) break;
            }
            if (brewingStandPos != null) break;
        }

        BlockPos targetPos = null;
        Direction targetFacing = null;
        int highestScore = Integer.MIN_VALUE;

        if (brewingStandPos != null) {
            // Search around the brewing stand / altar
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        BlockPos candidate = brewingStandPos.offset(dx, dy, dz);
                        if (candidate.equals(brewingStandPos) || candidate.above().equals(brewingStandPos)) {
                            continue;
                        }

                        if (!world.ensureCanWrite(candidate) || !world.ensureCanWrite(candidate.above())) {
                            continue;
                        }

                        BlockState belowState = world.getBlockState(candidate.below());
                        BlockState footState = world.getBlockState(candidate);
                        BlockState headState = world.getBlockState(candidate.above());

                        if (belowState.isAir() || !footState.isAir() || !headState.isAir()) {
                            continue;
                        }

                        // Evaluate best facing for this candidate position
                        for (Direction dir : Direction.Plane.HORIZONTAL) {
                            int score = 100 - (Math.abs(dx) + Math.abs(dz)) * 10;
                            BlockState backState = world.getBlockState(candidate.relative(dir.getOpposite()));
                            BlockState frontState = world.getBlockState(candidate.relative(dir));

                            if (!backState.isAir()) {
                                score += 50; // Backed against a wall
                            }
                            if (frontState.isAir()) {
                                score += 30; // Open in front for player interaction
                            }
                            if (candidate.getY() == brewingStandPos.getY()) {
                                score += 20; // Same floor level as brewing stand
                            }

                            if (score > highestScore) {
                                highestScore = score;
                                targetPos = candidate;
                                targetFacing = dir;
                            }
                        }
                    }
                }
            }
        }

        // 3. Fallback: If no brewing stand was in this chunk yet, try to find an interior floor spot near piecePos
        if (targetPos == null) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dz = -4; dz <= 4; dz++) {
                    for (int y = boundingBox.minY(); y <= boundingBox.maxY(); y++) {
                        BlockPos candidate = piecePos.offset(dx, y - piecePos.getY(), dz);
                        if (!boundingBox.isInside(candidate) || !world.ensureCanWrite(candidate) || !world.ensureCanWrite(candidate.above())) {
                            continue;
                        }

                        BlockState belowState = world.getBlockState(candidate.below());
                        BlockState footState = world.getBlockState(candidate);
                        BlockState headState = world.getBlockState(candidate.above());

                        if (belowState.isAir() || !footState.isAir() || !headState.isAir()) {
                            continue;
                        }

                        for (Direction dir : Direction.Plane.HORIZONTAL) {
                            int score = 50;
                            BlockState backState = world.getBlockState(candidate.relative(dir.getOpposite()));
                            BlockState frontState = world.getBlockState(candidate.relative(dir));
                            if (!backState.isAir()) score += 40;
                            if (frontState.isAir()) score += 20;

                            if (score > highestScore) {
                                highestScore = score;
                                targetPos = candidate;
                                targetFacing = dir;
                            }
                        }
                    }
                }
            }
        }

        // 4. Place the 2-block structure if a valid position was found
        if (targetPos != null && targetFacing != null) {
            BlockState lower = ModBlocks.MEDICAL_VENDING_MACHINE.defaultBlockState()
                .setValue(MedicalVendingMachineBlock.FACING, targetFacing)
                .setValue(MedicalVendingMachineBlock.HALF, DoubleBlockHalf.LOWER);
            BlockState upper = ModBlocks.MEDICAL_VENDING_MACHINE.defaultBlockState()
                .setValue(MedicalVendingMachineBlock.FACING, targetFacing)
                .setValue(MedicalVendingMachineBlock.HALF, DoubleBlockHalf.UPPER);

            world.setBlock(targetPos, lower, 3);
            world.setBlock(targetPos.above(), upper, 3);
            Antidote.LOGGER.info("Spawned Medical Vending Machine at {} (facing {}) inside village church ({})",
                targetPos, targetFacing, templateLocation);
        }
    }
}
