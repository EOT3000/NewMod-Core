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
import me.fly.newmod.flyfun.camera.Camera;
import me.fly.newmod.flyfun.camera.Textures;
import me.fly.newmod.flyfun.history.HistoryListener;
import me.fly.newmod.flyfun.horn.HornListener;
import me.fly.newmod.flyfun.magic.MagicTypes;
import me.fly.newmod.flyfun.magic.listener.AltarListener;
import me.fly.newmod.flyfun.magic.listener.SoulToolListener;
import me.fly.newmod.flyfun.magic.recipe.AltarRecipeManager;
import me.fly.newmod.flyfun.metals.MetalsTypes;
import me.fly.newmod.flyfun.plants.PlantsTypes;
import me.fly.newmod.flyfun.plants.listener.PlantsListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FlyFunPlugin extends JavaPlugin implements NewModAddon {
    public NewModAPI api;

    private static FlyFunPlugin instance;
    private BooksManager books;
    private GoldPanManager goldPan;
    private AltarRecipeManager altarRecipe;

    private final File textureDir = new File("/plugins/FlyFun/resources/block/textures");
    private final File modelDir = new File("/plugins/FlyFun/resources/block/models");
    private final File blockStatesDir = new File("/plugins/FlyFun/resources/block/blockstates");

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
        Bukkit.getPluginManager().registerEvents(new HistoryListener(), this);

        getLogger().info("Loading textures");
        Textures.me.loadTextures(textureDir);

        getLogger().info("Loading models");
        Textures.me.loadTextures(modelDir);

        getLogger().info("Loading block states");
        Textures.me.loadTextures(blockStatesDir);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HornListener.playAt(new Location(((Player) sender).getWorld(), 0, 64, 0), Sound.ITEM_GOAT_HORN_SOUND_0);

        byte[][] camera = Camera.run(((Player) sender).getLocation());

        ItemStack stack = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) stack.getItemMeta();

        MapView view = Bukkit.createMap(((Player) sender).getWorld());

        view.setTrackingPosition(false);

        for(MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }

        view.addRenderer(new Camera.Renderer(camera));

        meta.setMapView(view);

        stack.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(stack);

        return true;
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
