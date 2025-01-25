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
     * Returns if administrators may have split sections within this chunk.
     * That is, whether or not one {@link LandAdministrator} may correspond to two, different {@link PlotSection}s within this chunk.
     *
     * @return true if this chunk supports split administrators, false if not.
     */
    default boolean supportsSplitAdministrators() {
        return false;
    }

    /**
     * Returns the {@link PlotSection} at the specified coordinates in the chunk.
     *
     * @param x0_15 the x coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @param z0_15 the z coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @return the {@link PlotSection} of the given block, or {@code null} if not claimed.
     */
    @Nullable PlotSection getAt(int x0_15, int z0_15);

    /**
     * Sets the position specified within the chunk to a new plot section, of specified administration.
     * <p>
     * Precision depends on the specific implementation. For a 1x1, unsplit chunk, this should replace the whole chunk.
     *
     * @param x0_15 the x coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @param z0_15 the z coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @return the {@link PlotSection} that previously occupied that spot.
     */
    @Nullable PlotSection setAt(int x0_15, int z0_15, LandAdministrator admin);

    /**
     * Sets the position specified within the chunk to a new plot section.
     * <p>
     * Precision depends on the specific implementation. For a 1x1, unsplit chunk, this should replace the whole chunk.
     * <p>
     * This method must check {@link PlotSection#in()} to ensure that the chunk of the plot section matches itself.
     *
     * @throws UnsupportedOperationException if this chunk does not support split administrators, and the land administrator already has a plot section in this chunk.
     * @throws IllegalArgumentException if the provided PlotSection is invalid.
     * @param x0_15 the x coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @param z0_15 the z coordinate of the block, within the chunk, from 0 to 15. Corresponds with the in-chunk coordinate shown in-game in f3.
     * @return the {@link PlotSection} that previously occupied that spot.
     */
    @Nullable PlotSection setAt(int x0_15, int z0_15, PlotSection section);


    PlotSection[] getSections(boolean includingNull);

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
