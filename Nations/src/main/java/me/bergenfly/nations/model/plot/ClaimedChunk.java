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

    public void setSectionAt(int x0_15, int z0_15, LandAdministrator administrator, LandAdministrator holder) {
        this.section = section;
    }

    /**
     * Returns the {@link PlotSection} at the specified coordinates in the chunk.
     *
     * @param x0_15 the x coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @param z0_15 the z coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @return the {@link PlotSection} of the given block, or {@code null} if not claimed.
     */
    public @Nullable PlotSection getPlotSectionAt(int x0_15, int z0_15) {
        return section;
    }
}
