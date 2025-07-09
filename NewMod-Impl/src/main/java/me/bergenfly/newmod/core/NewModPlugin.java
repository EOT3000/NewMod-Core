package me.bergenfly.newmod.core;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.addon.NewModAddon;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.category.CategoryManager;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.block.BlockManagerImpl;
import me.bergenfly.newmod.core.blockreplacer.BlockReplacementManager;
import me.bergenfly.newmod.core.blockstorage.BlockStorageImpl;
import me.bergenfly.newmod.core.command.CheatCommand;
import me.bergenfly.newmod.core.item.category.CategoryManagerImpl;
import me.bergenfly.newmod.core.listener.*;
import me.bergenfly.newmod.core.item.ItemManagerImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
    private BlockReplacementManager blockReplacementManager;

    private CheatCommand cheatCommand;

    private ModItemCategory category;

    @Override
    public void onEnable() {
        this.blockReplacementManager = new BlockReplacementManager();


        this.blockStorage = new BlockStorageImpl();
        this.blockManager = new BlockManagerImpl();
        this.itemManager = new ItemManagerImpl();
        this.categoryManager = new CategoryManagerImpl();

        this.cheatCommand = new CheatCommand();

        System.out.println("yes it loaded");
        Bukkit.getPluginManager().registerEvents(new CheatInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new VanillaReplacementListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new DurabilityListener(), this);
        Bukkit.getPluginManager().registerEvents(new MiningLevelListener(), this);

        category = categoryManager.createCategory(new NamespacedKey(instance, "all_items_category"), Material.COMPASS, Component.text("All Items").color(NamedTextColor.GRAY));
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
    public GearManager gearManager() {
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

    public ModItemCategory getCategory() {
        return category;
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
