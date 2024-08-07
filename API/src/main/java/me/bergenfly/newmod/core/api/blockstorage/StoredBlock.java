package me.bergenfly.newmod.core.api.blockstorage;

import org.bukkit.NamespacedKey;

import java.util.Set;

/**
 * A stored block. May contain data.
 */
public interface StoredBlock {
    /**
     * Gets if this block has the provided key stored.
     *
     * @param key the key to check.
     * @param type the storage type.
     * @return true if the key is found on the block, false if not.
     */
    boolean hasData(NamespacedKey key, BlockStorage.StorageType type);

    /**
     * Gets all keys set for this block.
     *
     * @param type the storage type.
     * @return the set of keys stored at this block. If this block has no data, returns an empty list.
     */
    Set<NamespacedKey> getKeys(BlockStorage.StorageType type);

    /**
     * Gets the data at the provided key.
     *
     * @param key the key of the data.
     * @param type the storage type.
     * @return the data stored at the provided key. If the block has no stored data, may return null. Unset keys may return null or an empty string.
     */
    String getData(NamespacedKey key, BlockStorage.StorageType type);

    /**
     * Sets a value to this block. This method fails silently if invalid data is provided.
     *
     * @param key the key to set.
     * @param value the value to set.
     * @param type the storage type.
     */
    void setData(NamespacedKey key, String value, BlockStorage.StorageType type);

    /**
     * Removes a piece of data from this block.
     *
     * @param key the key for which data should be removed.
     * @param type the type of storage to remove from.
     */
    void removeData(NamespacedKey key, BlockStorage.StorageType type);

    /**
     * Removes all data of a type from this block.
     *
     * @param type the storage type to purge.
     */
    void removeAllData(BlockStorage.StorageType type);
}
