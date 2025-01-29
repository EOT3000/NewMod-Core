package me.bergenfly.nations.impl.util;

public record ChunkLocation(int x, int z) {
    public int chunkX() {
        return (int) Math.floor(x/16.0);
    }

    public int chunkZ() {
        return (int) Math.floor(z/16.0);
    }

    public int coordWithinChunkX() {
        return x-chunkX()*16;
    }

    public int coordWithinChunkZ() {
        return z-chunkZ()*16;
    }

    public int minCoordChunkX() {
        return chunkX()*16;
    }

    public int minCoordChunkZ() {
        return chunkZ()*16;
    }

    public int maxCoordChunkX() {
        return chunkX()*16+15;
    }

    public int maxCoordChunkZ() {
        return chunkZ()*16+15;
    }

    public int minCoordQuarterX() {
        return (int) Math.floor(x/8.0)*8;
    }

    public int minCoordQuarterZ() {
        return (int) Math.floor(z/8.0)*8;
    }

    public int maxCoordQuarterX() {
        return (int) Math.floor(x/8.0)*8+7;
    }

    public int maxCoordQuarterZ() {
        return (int) Math.floor(z/8.0)*8+7;
    }

    public int minCoordQuarterWithinChunkX() {
        return minCoordQuarterX()-chunkX()*16;
    }

    public int minCoordQuarterWithinChunkZ() {
        return minCoordQuarterZ()-chunkZ()*16;
    }

    public int maxCoordQuarterWithinChunkX() {
        return maxCoordQuarterX()-chunkX()*16;
    }

    public int maxCoordQuarterWithinChunkZ() {
        return maxCoordQuarterZ()-chunkZ()*16;
    }
}
