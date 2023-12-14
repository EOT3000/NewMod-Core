package me.fly.newmod.core.api.block;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

/**
 * A singleton which holds the block registry and provides mod block utility methods.
 */
public interface BlockManager {
    /**
     * Registers a block to this manager.
     *
     * @param block the block to register.
     */
    void registerBlock(ModBlock block);

    /**
     * Attempts to find the ModBlock at the provided block.
     *
     * @param block the block to check.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(Block block);

    /**
     * Attempts to find the ModBlock at the provided block.
     *
     * @param block the block to check.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(Location block);

    /**
     * Attempts to find the ModBlock of this NamespacedKey
     *
     * @param key the key.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(NamespacedKey key);

    /**
     * Sets the block type of this block. This method overrides previous data and does not physically place the block in the world.
     *
     * @param block the block to set.
     * @param type the type to set to.
     */
    void setBlock(Block block, ModBlock type);
}
