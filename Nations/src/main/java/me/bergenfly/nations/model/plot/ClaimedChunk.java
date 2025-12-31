package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.model.LandAdministrator;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class ClaimedChunk {
    private DivisionStorage storage;

    private final int chunkX;
    private final int chunkZ;
    private final World world;

    public ClaimedChunk(int chunkX, int chunkZ, World world) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public World getWorld() {
        return world;
    }

    public DivisionStorage getStorage() {
        return storage;
    }
}
