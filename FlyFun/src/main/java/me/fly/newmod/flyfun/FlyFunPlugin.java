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
import me.fly.newmod.flyfun.books.listener.TreeBarkListener;
import me.fly.newmod.flyfun.camera.Camera;
import me.fly.newmod.flyfun.camera.Textures;
import me.fly.newmod.flyfun.camera.model.BlockModel;
import me.fly.newmod.flyfun.camera.model.AllSidesBlockModel;
import me.fly.newmod.flyfun.fortunefix.FortuneListener;
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
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.io.File;

public class FlyFunPlugin extends JavaPlugin implements NewModAddon {
    public NewModAPI api;

    private static FlyFunPlugin instance;
    private BooksManager books;
    private GoldPanManager goldPan;
    private AltarRecipeManager altarRecipe;

    private final File textureDir = new File("plugins/FlyFun/resources/block/textures");
    private final File modelDir = new File("plugins/FlyFun/resources/block/models");
    private final File blockStatesDir = new File("plugins/FlyFun/resources/block/blockstates");

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
        Bukkit.getPluginManager().registerEvents(new FortuneListener(), this);
        Bukkit.getPluginManager().registerEvents(new TreeBarkListener(), this);

        System.out.println(new File("").getAbsolutePath());
        System.out.println(textureDir.getAbsolutePath());

        getLogger().info("Loading textures");
        Textures.me.loadTextures(textureDir);

        getLogger().info("Loading models");
        Textures.me.loadModels(modelDir);

        getLogger().info("Loading block states");
        Textures.me.loadBlockStates(blockStatesDir);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 5) {
            Player p = (Player) sender;

            Location loc = new Location(p.getWorld(),
                    Double.parseDouble(args[0]),
                    Double.parseDouble(args[1]),
                    Double.parseDouble(args[2])
            );

            BlockDisplay display = p.getWorld().spawn(loc, BlockDisplay.class);

            display.setBlock(Material.STONE.createBlockData());
            display.setTransformation(new Transformation(new Vector3f(0,0,0), new AxisAngle4f(), new Vector3f(0.1f,0.1f,0.1f), new AxisAngle4f()));

            Vector vec = loc.toVector().subtract(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())).subtract(new Vector(0.5f,0.5f,0.5f));

            vec.rotateAroundY(Math.toRadians(-Integer.parseInt(args[4])));
            vec.rotateAroundX(Math.toRadians(-Integer.parseInt(args[3])));

            vec.add(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())).subtract(new Vector(0.5f,0.5f,0.5f));

            BlockDisplay display2 = p.getWorld().spawn(new Location(p.getWorld(), vec.getX(), vec.getY(), vec.getZ()), BlockDisplay.class);

            display2.setBlock(Material.GOLD_BLOCK.createBlockData());
            display2.setTransformation(new Transformation(new Vector3f(0,0,0), new AxisAngle4f(), new Vector3f(0.1f,0.1f,0.1f), new AxisAngle4f()));

            return true;
        }

        //System.out.println("sky brightness: " + NMSUtil.getSkyBrightness(((Player) sender).getWorld()));

        if(args.length == 1) {
            BlockModel model = Textures.me.getStates(Material.getMaterial(args[0])).getStates().get(0).model();

            byte[][] camera = new byte[128][128];

            if(model instanceof AllSidesBlockModel) {

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y] = model.getMapColor(x, y, BlockFace.NORTH, null, b);
                        }

                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y+16] = model.getMapColor(x, y, BlockFace.NORTH, null, b+8);
                        }
                    }
                }

            } else {

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y] = model.getMapColor(x, y, BlockFace.UP, null, b);
                        }

                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y+16] = model.getMapColor(x, y, BlockFace.UP, null, b+8);
                        }

                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y+32] = model.getMapColor(x, y, BlockFace.EAST, null, b);
                        }

                        for (int b = 0; b < 8; b++) {
                            camera[b*16+x][y+48] = model.getMapColor(x, y, BlockFace.EAST, null, b+8);
                        }
                    }
                }

            }
            giveToPlayer(camera, sender);

            return true;
        }

        HornListener.playAt(new Location(((Player) sender).getWorld(), 0, 64, 0), Sound.ITEM_GOAT_HORN_SOUND_0);

        getLogger().info("Beginning picture capture");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            byte[][] camera = Camera.run(((Player) sender).getEyeLocation());

            giveToPlayer(camera, sender);
        }, 1);

        return true;
    }

    private void giveToPlayer(byte[][] camera, CommandSender sender) {
        ItemStack stack = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) stack.getItemMeta();

        MapView view = Bukkit.createMap(((Player) sender).getWorld());

        view.setTrackingPosition(false);

        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }

        view.addRenderer(new Camera.Renderer(camera));

        view.setLocked(true);

        meta.setMapView(view);

        stack.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(stack);
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
