package me.fly.newmod.core;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.blockstorage.BlockStorageImpl;
import me.fly.newmod.core.item.ItemManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class NewModPlugin extends JavaPlugin implements NewModAPI {
    private ItemManagerImpl itemManager;
    private BlockStorageImpl blockStorage;

    @Override
    public void onEnable() {
        this.blockStorage = new BlockStorageImpl();
    }

    @Override
    public ItemManager itemManager() {
        return itemManager;
    }

    @Override
    public BlockStorage blockStorage() {
        return blockStorage;
    }

    public static NewModPlugin get() {
        return null;
    }
}
