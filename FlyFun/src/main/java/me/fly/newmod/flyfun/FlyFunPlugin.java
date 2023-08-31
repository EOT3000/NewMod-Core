package me.fly.newmod.flyfun;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.addon.NewModAddon;
import me.fly.newmod.flyfun.basictools.BasicToolsTypes;
import me.fly.newmod.flyfun.basictools.GoldPanManager;
import me.fly.newmod.flyfun.basictools.listener.BasicToolsListener;
import me.fly.newmod.flyfun.books.BooksManager;
import me.fly.newmod.flyfun.books.BooksTypes;
import me.fly.newmod.flyfun.books.data.WritableItemData;
import me.fly.newmod.flyfun.books.data.WritableItemDataImpl;
import me.fly.newmod.flyfun.books.listener.BooksListener;
import me.fly.newmod.flyfun.magic.MagicTypes;
import me.fly.newmod.flyfun.magic.listener.AltarListener;
import me.fly.newmod.flyfun.magic.listener.SoulToolListener;
import me.fly.newmod.flyfun.magic.recipe.AltarRecipeManager;
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
    private GoldPanManager goldPan;
    private AltarRecipeManager altarRecipe;

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
        this.goldPan = new GoldPanManager();
        this.altarRecipe = new AltarRecipeManager();

        MetalsTypes.init();
        PlantsTypes.init();
        BooksTypes.init();
        BasicToolsTypes.init();
        MagicTypes.init();

        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemData.class);
        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemDataImpl.class);

        Bukkit.getPluginManager().registerEvents(new PlantsListener(), this);
        Bukkit.getPluginManager().registerEvents(new BooksListener(), this);
        Bukkit.getPluginManager().registerEvents(new BasicToolsListener(), this);
        Bukkit.getPluginManager().registerEvents(new AltarListener(), this);
        Bukkit.getPluginManager().registerEvents(new SoulToolListener(), this);
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

    public GoldPanManager getGoldPanManager() {
        return goldPan;
    }

    public AltarRecipeManager getAltarRecipeManager() {
        return altarRecipe;
    }
}
