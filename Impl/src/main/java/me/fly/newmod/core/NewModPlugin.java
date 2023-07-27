package me.fly.newmod.core;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.addon.NewModAddon;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.category.CategoryManager;
import me.fly.newmod.core.block.BlockManagerImpl;
import me.fly.newmod.core.blockstorage.BlockStorageImpl;
import me.fly.newmod.core.command.CheatCommand;
import me.fly.newmod.core.item.ItemManagerImpl;
import me.fly.newmod.core.item.category.CategoryManagerImpl;
import me.fly.newmod.core.listener.BlockListener;
import me.fly.newmod.core.listener.CheatInventoryListener;
import me.fly.newmod.core.listener.VanillaReplacementListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NewModPlugin extends JavaPlugin implements NewModAPI {
    private static NewModPlugin instance;

    private ItemManagerImpl itemManager;
    private BlockManagerImpl blockManager;
    private BlockStorageImpl blockStorage;
    private CategoryManagerImpl categoryManager;

    private CheatCommand cheatCommand;

    @Override
    public void onEnable() {
        this.blockStorage = new BlockStorageImpl();
        this.blockManager = new BlockManagerImpl();
        this.itemManager = new ItemManagerImpl();
        this.categoryManager = new CategoryManagerImpl();

        this.cheatCommand = new CheatCommand();

        Bukkit.getPluginManager().registerEvents(new CheatInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new VanillaReplacementListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player && player.isOp()) {
            if(args.length < 1 || !args[0].equalsIgnoreCase("cheat")) {
                return false;
            }

            cheatCommand.run(player);

            return true;
        }

        return false;
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
