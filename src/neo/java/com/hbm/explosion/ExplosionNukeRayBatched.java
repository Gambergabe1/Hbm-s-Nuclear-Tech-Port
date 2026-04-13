package com.hbm.explosion;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExplosionNukeRayBatched {
    public final HashMap<ChunkPos, BitSet> perChunk = new HashMap<>();
    public final List<ChunkPos> orderedChunks = new ArrayList<>();
    private final CoordComparator comparator = new CoordComparator();

    public boolean isContained = true;
    public boolean ignoreWater = false;
    public boolean isAusf3Complete = false;
    public int rayCheckInterval = 100;
    public int waterLevel;

    double posX;
    double posY;
    double posZ;
    Level world;
    int strength;
    int radius;

    private int gspNumMax;
    private int gspNum;
    private double gspX;
    private double gspY;
    private final int minBuildHeight;
    private final int topBuildHeight;
    private final int heightSpan;

    private BitSet hitArray;
    private ChunkPos chunk;
    private boolean needsNewHitArray = true;
    private int index = 0;

    public ExplosionNukeRayBatched(Level world, double x, double y, double z, int strength, int radius, boolean ignoreWater) {
        this.world = world;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.strength = strength;
        this.radius = radius;
        this.ignoreWater = ignoreWater;
        this.gspNumMax = (int) (2.5D * Math.PI * Math.pow(Math.max(1, strength), 2));
        this.gspNum = 1;
        this.gspX = Math.PI;
        this.gspY = 0.0D;
        this.rayCheckInterval = Math.max(1, 10000 / Math.max(1, radius));
        this.waterLevel = world.getSeaLevel();
        this.minBuildHeight = world.getMinBuildHeight();
        this.topBuildHeight = world.getMaxBuildHeight() - 1;
        this.heightSpan = world.getMaxBuildHeight() - world.getMinBuildHeight();
    }

    private void generateGspUp() {
        if (this.gspNum >= this.gspNumMax) {
            this.gspX = 0.0D;
            this.gspY = 0.0D;
            this.gspNum++;
            return;
        }

        int next = this.gspNum + 1;
        double hk = -1.0D + 2.0D * (next - 1.0D) / Math.max(1.0D, this.gspNumMax - 1.0D);
        this.gspX = Math.acos(hk);
        double delta = 3.6D / Math.sqrt(Math.max(1.0D, this.gspNumMax)) / Math.sqrt(Math.max(1.0E-6D, 1.0D - hk * hk));
        this.gspY = (this.gspY + delta) % (Math.PI * 2.0D);
        this.gspNum++;
    }

    private Vec3 getSpherical2cartesian() {
        double dx = Math.sin(this.gspX) * Math.cos(this.gspY);
        double dy = Math.sin(this.gspX) * Math.sin(this.gspY);
        double dz = Math.cos(this.gspX);
        return new Vec3(dx, dy, dz);
    }

    public void addPos(int x, int y, int z) {
        ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
        BitSet hitPositions = perChunk.computeIfAbsent(chunkPos, ignored -> new BitSet(16 * 16 * this.heightSpan));
        hitPositions.set(((this.topBuildHeight - y) << 8) + ((x - chunkPos.x * 16) << 4) + (z - chunkPos.z * 16));
    }

    public boolean waterCheck(Block block, int y) {
        return block != Blocks.AIR && (!this.ignoreWater || y >= this.waterLevel);
    }

    public void collectTip(int time) {
        if (this.isAusf3Complete) {
            return;
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        long deadline = System.currentTimeMillis() + Math.max(1L, time);
        long raysProcessed = 0L;

        while (this.gspNum <= this.gspNumMax) {
            Vec3 vec = this.getSpherical2cartesian();
            int localRadius = Math.max(1, this.radius);
            float rayStrength = this.strength * 0.3F;

            for (int r = 0; r <= localRadius; r++) {
                int blockY = Mth.floor(this.posY + vec.y * r);
                if (blockY < this.minBuildHeight || blockY > this.topBuildHeight) {
                    this.isContained = false;
                    break;
                }

                int blockX = Mth.floor(this.posX + vec.x * r);
                int blockZ = Mth.floor(this.posZ + vec.z * r);
                pos.set(blockX, blockY, blockZ);

                if (!this.world.isLoaded(pos)) {
                    this.isContained = false;
                    break;
                }

                BlockState blockState = this.world.getBlockState(pos);
                Block block = blockState.getBlock();
                if (block.getExplosionResistance() >= 2_000_000.0F) {
                    break;
                }

                rayStrength -= (float) (
                    Math.pow(getNukeResistance(blockState, block) + 1.0F, 3.0D * r / Math.max(1.0D, localRadius)) - 1.0D
                );

                if (rayStrength > 0.0F) {
                    if (waterCheck(block, blockY)) {
                        addPos(blockX, blockY, blockZ);
                    }
                    if (r >= localRadius) {
                        this.isContained = false;
                    }
                } else {
                    break;
                }
            }

            this.generateGspUp();
            raysProcessed++;
            if (raysProcessed % this.rayCheckInterval == 0 && System.currentTimeMillis() + 1L > deadline) {
                return;
            }
        }

        this.orderedChunks.clear();
        this.orderedChunks.addAll(this.perChunk.keySet());
        this.orderedChunks.sort(this.comparator);
        this.isAusf3Complete = true;
    }

    public static float getNukeResistance(BlockState blockState, Block block) {
        if (!blockState.getFluidState().isEmpty()) {
            return 0.1F;
        }
        if (blockState.is(Blocks.SANDSTONE)) {
            return 4.0F;
        }
        if (blockState.is(Blocks.OBSIDIAN)) {
            return 18.0F;
        }
        return Math.max(0.1F, block.getExplosionResistance());
    }

    public void processChunk(int time) {
        long deadline = System.currentTimeMillis() + Math.max(1L, time);
        while (System.currentTimeMillis() < deadline && !this.perChunk.isEmpty()) {
            processChunkBlocks(deadline, time);
        }
    }

    public int processChunkBlocks(long start, int time) {
        if (this.perChunk.isEmpty()) {
            return 0;
        }

        if (this.needsNewHitArray) {
            this.chunk = this.orderedChunks.isEmpty() ? null : this.orderedChunks.get(0);
            if (this.chunk == null) {
                this.perChunk.clear();
                return 0;
            }
            this.hitArray = this.perChunk.get(this.chunk);
            this.index = this.hitArray.nextSetBit(0);
            this.needsNewHitArray = false;
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int chunkXStart = this.chunk.x * 16;
        int chunkZStart = this.chunk.z * 16;
        int removedBlocks = 0;

        while (this.index >= 0 && System.currentTimeMillis() <= start + Math.max(1L, time)) {
            pos.set(chunkXStart + ((this.index >> 4) % 16), this.topBuildHeight - (this.index >> 8), chunkZStart + (this.index % 16));
            if (this.world.isLoaded(pos)) {
                this.world.destroyBlock(pos, false, null);
                removedBlocks++;
            }
            this.index = this.hitArray.nextSetBit(this.index + 1);
        }

        if (this.index < 0) {
            this.perChunk.remove(this.chunk);
            if (!this.orderedChunks.isEmpty()) {
                this.orderedChunks.remove(0);
            }
            this.needsNewHitArray = true;
        }

        return removedBlocks;
    }

    public void collectAll() {
        while (!this.isAusf3Complete) {
            collectTip(Integer.MAX_VALUE / 4);
        }
    }

    public int processAll() {
        int removedBlocks = 0;
        while (!this.perChunk.isEmpty()) {
            removedBlocks += processChunkBlocks(Long.MAX_VALUE / 4L, Integer.MAX_VALUE / 4);
        }
        return removedBlocks;
    }

    public void load(CompoundTag tag) {
        this.radius = tag.getInt("radius");
        this.strength = tag.getInt("strength");
        this.posX = tag.getDouble("posX");
        this.posY = tag.getDouble("posY");
        this.posZ = tag.getDouble("posZ");
        this.ignoreWater = tag.getBoolean("igW");
        this.gspNumMax = (int) (2.5D * Math.PI * Math.pow(Math.max(1, this.strength), 2));
        this.gspNum = tag.getInt("gspNum");
        this.isAusf3Complete = tag.getBoolean("f3");
        this.isContained = tag.getBoolean("isContained");
        this.waterLevel = this.world.getSeaLevel();

        this.perChunk.clear();
        this.orderedChunks.clear();
        int i = 0;
        while (tag.contains("chunks" + i)) {
            CompoundTag chunkTag = tag.getCompound("chunks" + i);
            ChunkPos chunkPos = new ChunkPos(chunkTag.getInt("cX"), chunkTag.getInt("cZ"));
            this.perChunk.put(chunkPos, BitSet.valueOf(chunkTag.getLongArray("cB")));
            i++;
        }

        if (this.isAusf3Complete) {
            this.orderedChunks.addAll(this.perChunk.keySet());
            this.orderedChunks.sort(this.comparator);
        }
        this.needsNewHitArray = true;
    }

    public void save(CompoundTag tag) {
        tag.putInt("radius", this.radius);
        tag.putInt("strength", this.strength);
        tag.putDouble("posX", this.posX);
        tag.putDouble("posY", this.posY);
        tag.putDouble("posZ", this.posZ);
        tag.putBoolean("igW", this.ignoreWater);
        tag.putInt("gspNum", this.gspNum);
        tag.putBoolean("f3", this.isAusf3Complete);
        tag.putBoolean("isContained", this.isContained);

        int i = 0;
        for (Entry<ChunkPos, BitSet> entry : this.perChunk.entrySet()) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putInt("cX", entry.getKey().x);
            chunkTag.putInt("cZ", entry.getKey().z);
            chunkTag.putLongArray("cB", entry.getValue().toLongArray());
            tag.put("chunks" + i, chunkTag);
            i++;
        }
    }

    public class CoordComparator implements Comparator<ChunkPos> {
        @Override
        public int compare(ChunkPos left, ChunkPos right) {
            int chunkX = ((int) ExplosionNukeRayBatched.this.posX) >> 4;
            int chunkZ = ((int) ExplosionNukeRayBatched.this.posZ) >> 4;
            int distanceLeft = Math.abs(chunkX - left.x) + Math.abs(chunkZ - left.z);
            int distanceRight = Math.abs(chunkX - right.x) + Math.abs(chunkZ - right.z);
            return Integer.compare(distanceLeft, distanceRight);
        }
    }
}
