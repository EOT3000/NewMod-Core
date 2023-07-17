package me.fly.newmod.core.api.blockstorage;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.Set;

/**
 * A singleton which holds location data for the server.
 */
public interface BlockStorage {
    /**
     * Gets if the provided location has the provided key stored.
     *
     * @param location the location to check.
     * @param key the key to check.
     * @return true if the key is found on the block, false if not.
     */
    boolean hasData(Location location, NamespacedKey key);

    /**
     * Gets all keys set for this block.
     *
     * @param location the location from where to get the keys.
     * @return the set of keys stored at this block. If this block has no data, returns an empty list.
     */
    Set<NamespacedKey> getKeys(Location location);

    /**
     * Gets the data at the provided location and key.
     *
     * @param location the location from where to get the data.
     * @param key the key of the data.
     * @return the data stored at the provided location and key. If the block has no stored data, may return null. Unset keys may return null or an empty string.
     */
    String getData(Location location, NamespacedKey key);

    /**
     * Sets a value to a block. This method fails silently if invalid data is provided.
     *
     * @param location the location to set.
     * @param key the key to set.
     * @param value the value to set.
     */
    void setData(Location location, NamespacedKey key, String value);

    /**
     * Removes a piece of data from the provided block.
     *
     * @param location the location from where the data should be removed.
     * @param key the key for which data should be removed.
     */
    void removeData(Location location, NamespacedKey key);

    /**
     * Gets all the blocks stored in this storage, in the provided world.
     *
     * @param world the world from which to get all stored locations.
     * @return a set of all the blocks stored in this storage.
     */
    Set<Location> getAllStoredLocations(World world);
}
