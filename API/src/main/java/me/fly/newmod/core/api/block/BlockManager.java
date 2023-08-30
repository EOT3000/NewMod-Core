package me.fly.newmod.core.api.block;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

/**
 * A singleton which holds the block registry and provides mod block utility methods.
 */
public interface BlockManager {
    /**
     * Registers an block to this manager.
     *
     * @param block the block to register.
     */
    void registerBlock(ModBlock block);

    /**
     * Attempts to find this BlockStack's ModBlock.
     *
     * @param block the BlockStack to check.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(Block block);

    /**
     * Attempts to find the ModBlock of this NamespacedKey
     *
     * @param key the key.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(NamespacedKey key);

    /**
     * Sets the block type of this block. Overrides previous data.
     *
     * @param block the block to set.
     * @param type the type to set to.
     */
    void setBlock(Block block, ModBlock type);
}
