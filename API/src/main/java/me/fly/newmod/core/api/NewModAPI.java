package me.fly.newmod.core.api;

import me.fly.newmod.core.api.item.ItemManager;

/**
 * The primary class for accessing the NewMod API.
 */
public interface NewModAPI {
    /**
     * @return the API's item manager.
     */
    ItemManager itemManager();

}
