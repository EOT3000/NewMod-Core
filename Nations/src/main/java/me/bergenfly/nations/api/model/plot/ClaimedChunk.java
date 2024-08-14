package me.bergenfly.nations.api.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A chunk of land claimed by one, or multiple {@link LandAdministrator}s.
 * <p>
 * Chunks may be divided into 16 4x4 squares, in order to better match borders.
 * <p>
 * A group of 4x4 squares in a chunk owned by the same entity is known as a {@link PlotSection}
 */
public interface ClaimedChunk {

    /**
     * Returns the {@link PlotSection} at the specified coordinates in the chunk.
     *
     * @param x0_15 the x coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @param z0_15 the z coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @return the {@link PlotSection} of the given block, or {@code null} if not claimed.
     */
    @Nullable PlotSection getAt(int x0_15, int z0_15);

    /**
     * Returns the x coordinate of this chunk.
     *
     * @return the x coordinate of this chunk.
     */
    int getChunkX();

    /**
     * Returns the z coordinate of this chunk.
     *
     * @return the z coordinate of this chunk.
     */
    int getChunkZ();

    /**
     * Returns the world of this chunk
     *
     * @return the world of this chunk.
     */
    @NotNull World getWorld();

    /**
     * Gets the divisions of the chunk (how many times the side is divided in half). {@code 0} means the chunk is undivided, {@code 1} means the chunk is split 2x2, etc.
     *
     * @return the divisions of the chunk.
     */
    int getDivision();

    void unclaim();
}
