package me.fly.newmod.core;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.addon.NewModAddon;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.category.CategoryManager;
import me.fly.newmod.core.block.BlockManagerImpl;
import me.fly.newmod.core.blockstorage.BlockStorageImpl;
import me.fly.newmod.core.item.ItemManagerImpl;
import me.fly.newmod.core.item.category.CategoryManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class NewModPlugin extends JavaPlugin implements NewModAPI {
    private static NewModPlugin instance;

    private ItemManagerImpl itemManager;
    private BlockManagerImpl blockManager;
    private BlockStorageImpl blockStorage;
    private CategoryManagerImpl categoryManager;

    @Override
    public void onEnable() {
        this.blockStorage = new BlockStorageImpl();
        this.blockManager = new BlockManagerImpl();
        this.itemManager = new ItemManagerImpl();
        this.categoryManager = new CategoryManagerImpl();
    }

    @Override
    public ItemManager itemManager() {
        return itemManager;
    }

    @Override
    public BlockManager blockManager() {
        return blockManager;
    }

    @Override
    public BlockStorage blockStorage() {
        return blockStorage;
    }

    @Override
    public CategoryManager categoryManager() {
        return categoryManager;
    }

    @Override
    public void registerAddon(NewModAddon addon) {
        //nothing yet
    }

    public static NewModPlugin get() {
        return instance;
    }

    public NewModPlugin() {
        if(instance != null) {
            throw new RuntimeException("Attempted to create a second NewModPlugin instance.");
        }

        instance = this;
    }
}
