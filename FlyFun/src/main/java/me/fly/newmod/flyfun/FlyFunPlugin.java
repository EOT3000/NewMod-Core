package me.fly.newmod.flyfun;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.addon.NewModAddon;
import me.fly.newmod.flyfun.books.BooksManager;
import me.fly.newmod.flyfun.books.BooksTypes;
import me.fly.newmod.flyfun.books.data.WritableItemData;
import me.fly.newmod.flyfun.books.data.WritableItemDataImpl;
import me.fly.newmod.flyfun.books.listener.BooksListener;
import me.fly.newmod.flyfun.metals.MetalsTypes;
import me.fly.newmod.flyfun.plants.PlantsTypes;
import me.fly.newmod.flyfun.plants.listener.PlantsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyFunPlugin extends JavaPlugin implements NewModAddon {
    public NewModAPI api;

    private static FlyFunPlugin instance;
    private BooksManager books;

    @Override
    public void onLoad() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("NewMod-Core");

        if(plugin == null) {
            throw new RuntimeException("NewMod is not present");
        }

        api = (NewModAPI) plugin;

        api.registerAddon(this);
    }

    @Override
    public void onEnable() {
        this.books = new BooksManager();

        MetalsTypes.init();
        PlantsTypes.init();
        BooksTypes.init();

        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemData.class);
        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemDataImpl.class);

        Bukkit.getPluginManager().registerEvents(new PlantsListener(), this);
        Bukkit.getPluginManager().registerEvents(new BooksListener(), this);
    }

    public FlyFunPlugin() {
        if(instance != null) {
            throw new RuntimeException("Attempted to create a second FlyFunPlugin instance.");
        }

        instance = this;
    }

    public static FlyFunPlugin get() {
        return instance;
    }

    public BooksManager getBooksManager() {
        return books;
    }
}
