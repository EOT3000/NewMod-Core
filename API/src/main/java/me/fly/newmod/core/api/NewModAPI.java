package me.fly.newmod.core.api;

import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ItemManager;

/**
 * The primary class for accessing the NewMod API.
 */
public interface NewModAPI {
    /**
     * @return the API's item manager.
     */
    ItemManager itemManager();

    /**
     * @return the API's block manager.
     */
    BlockManager blockManager();

    /**
     * @return the API's block storage.
     */
    BlockStorage blockStorage();
}
