package me.bergenfly.nations.api.model;

import org.bukkit.Chunk;
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
     * Returns the Bukkit {@link Chunk} of this ClaimedChunk object.
     *
     * @return the {@link Chunk} of this object.
     */
    @NotNull Chunk getChunk();
}
