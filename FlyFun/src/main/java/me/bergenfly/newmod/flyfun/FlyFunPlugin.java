package me.bergenfly.newmod.flyfun;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.addon.NewModAddon;
import me.bergenfly.newmod.core.util.skytest;
import me.bergenfly.newmod.flyfun.basictools.BasicToolsTypes;
import me.bergenfly.newmod.flyfun.basictools.GoldPanManager;
import me.bergenfly.newmod.flyfun.basictools.listener.BasicToolsListener;
import me.bergenfly.newmod.flyfun.books.BooksManager;
import me.bergenfly.newmod.flyfun.books.BooksTypes;
import me.bergenfly.newmod.flyfun.books.data.WritableItemData;
import me.bergenfly.newmod.flyfun.books.data.WritableItemDataImpl;
import me.bergenfly.newmod.flyfun.books.listener.BooksListener;
import me.bergenfly.newmod.flyfun.books.listener.TreeBarkListener;
import me.bergenfly.newmod.flyfun.camera.Camera;
import me.bergenfly.newmod.flyfun.camera.Textures;
import me.bergenfly.newmod.flyfun.camera.model.BlockModel;
import me.bergenfly.newmod.flyfun.camera.model.AllSidesBlockModel;
import me.bergenfly.newmod.flyfun.camera.model.BlockStates;
import me.bergenfly.newmod.flyfun.food.AnimalsTypes;
import me.bergenfly.newmod.flyfun.food.FoodsTypes;
import me.bergenfly.newmod.flyfun.food.listener.FoodListener;
import me.bergenfly.newmod.flyfun.food.nutrient.VanillaFoods;
import me.bergenfly.newmod.flyfun.food.vanilla.VanillaPlantListener;
import me.bergenfly.newmod.flyfun.history.HistoryListener;
import me.bergenfly.newmod.flyfun.horn.HornListener;
import me.bergenfly.newmod.flyfun.magic.MagicTypes;
import me.bergenfly.newmod.flyfun.magic.listener.AltarListener;
import me.bergenfly.newmod.flyfun.magic.listener.SoulToolListener;
import me.bergenfly.newmod.flyfun.magic.recipe.AltarRecipeManager;
import me.bergenfly.newmod.flyfun.metals.MetalsTypes;
import me.bergenfly.newmod.flyfun.food.PlantsTypes;
import me.bergenfly.newmod.flyfun.food.listener.PlantsListener;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.BlockDisplay;
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
import java.util.logging.Level;

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
        AnimalsTypes.init();
        FoodsTypes.init();
        BooksTypes.init();
        BasicToolsTypes.init();
        MagicTypes.init();

        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemData.class);
        api.itemManager().registerSerializer(new WritableItemDataImpl.WritableItemDataSerializer(), WritableItemDataImpl.class);

        Bukkit.getPluginManager().registerEvents(new PlantsListener(), this);
        Bukkit.getPluginManager().registerEvents(new FoodListener(), this);
        Bukkit.getPluginManager().registerEvents(new BooksListener(), this);
        Bukkit.getPluginManager().registerEvents(new BasicToolsListener(), this);
        Bukkit.getPluginManager().registerEvents(new AltarListener(), this);
        Bukkit.getPluginManager().registerEvents(new SoulToolListener(), this);
        Bukkit.getPluginManager().registerEvents(new HistoryListener(), this);
        //Bukkit.getPluginManager().registerEvents(new FortuneListener(), this);
        Bukkit.getPluginManager().registerEvents(new TreeBarkListener(), this);
        Bukkit.getPluginManager().registerEvents(new VanillaPlantListener(), this);

        VanillaFoods.init();

        System.out.println(new File("").getAbsolutePath());
        //System.out.println(textureDir.getAbsolutePath());

        try {
            getLogger().info("Loading textures");
            Textures.me.loadTextures(textureDir);

            getLogger().info("Loading models");
            Textures.me.loadModels(modelDir);

            getLogger().info("Loading block states");
            Textures.me.loadBlockStates(blockStatesDir);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error Loading block states/models/textures");
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Player player = Bukkit.getOnlinePlayers().iterator().next();

            ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();

            float a = level.getTimeOfDay(1);
            float c = level.getDayTime();
            float b = player.getWorld().getTime();

            System.out.println("NMS Gives Sky angle: " + a);
            System.out.println("NMS Gives day time: " + c);
            System.out.println("GetTime: " + b);
            System.out.println();
        }, 1200, 1200);
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

            Material material = Material.getMaterial(args[0]);

            BlockStates states = Textures.me.getStates(material);

            for(BlockStates.BlockState state : states.getStates()) {
                sender.sendMessage("Model: " + state.model().texturesString());
            }

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

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("load")) {
                File file = new File("photo" + args[1]);

                Camera.loadFile(file, (Player) sender);
                return true;
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("data")) {
                Player player = (Player) sender;

                long time = player.getWorld().getTime();
                double tod = skytest.timeOfDay(time);
                double brightness = skytest.getSkyBrightness((float) tod);
                skytest.vec3 slc = skytest.mix(new skytest.vec3(brightness, brightness, 1.0), new skytest.vec3(1.0,1.0,1.0), .35);

                sender.sendMessage("Time: " + time);
                sender.sendMessage("Sky angle: " + tod);
                sender.sendMessage("Sky brightness: " + brightness);
                sender.sendMessage("Color: " + slc);
                return true;
            }
        }

        HornListener.playAt(new Location(((Player) sender).getWorld(), 0, 64, 0), Sound.ITEM_GOAT_HORN_SOUND_0);

        getLogger().info("Beginning picture capture");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Camera.run(((Player) sender).getEyeLocation());

            //giveToPlayer(camera, sender);
        }, 1);

        return true;
    }

    public void giveToPlayer(byte[][] camera, CommandSender sender) {
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
