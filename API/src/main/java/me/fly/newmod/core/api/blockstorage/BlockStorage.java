package me.fly.newmod.core.api.blockstorage;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Set;

/**
 * A singleton which holds location data for the server.
 */
public interface BlockStorage {

    /**
     * Gets the stored block at the provided location.
     *
     * @param location the location.
     * @return the stored block.
     */
    StoredBlock getBlock(Location location);

    /**
     * Gets all the blocks stored in this storage, in the provided chunk.
     *
     * @param chunk the chunk from which to get all stored locations.
     * @return a set of all the blocks stored in this storage and chun.
     */
    Set<Location> getAllStoredLocations(Chunk chunk);

    /**
     * An enum to represent the two types of stored data.
     */
    enum StorageType {
        /**
         * BLOCK_DATA is stored data used by mod blocks. Includes ID, energy, or anything else from an individual block.
         */
        BLOCK_DATA,
        /**
         * ENVIRONMENTAL is stored data used by anything else, without a specific block. May include things such as temperature.
         */
        ENVIRONMENTAL
    }
}
