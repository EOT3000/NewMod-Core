package me.fly.newmod.core.api;

import me.fly.newmod.core.api.addon.NewModAddon;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.category.CategoryManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The primary class for accessing the NewMod API.
 */
public interface NewModAPI {
    /**
     * @return the API's item manager.
     */
    ItemManager itemManager();

    /**
     * @return the API's gear manager.
     */
    GearManager gearManager();

    /**
     * @return the API's block manager.
     */
    BlockManager blockManager();

    /**
     * @return the API's block storage.
     */
    BlockStorage blockStorage();

    /**
     * @return the API's category manager.
     */
    CategoryManager categoryManager();

    /**
     * @return the API's event manager.
     */
    //EventManager eventManager();

    /**
     * Registers an addon. Should be done during {@link JavaPlugin#onLoad()}.
     *
     * @param addon the addon to register.
     */
    void registerAddon(NewModAddon addon);
}
