package me.bergenfly.newmod.flyfun.basictools;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.metals.MetalsTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

@SuppressWarnings("unused")
public class BasicToolsTypes {
    private static final String[] AXE = new String[]{
            "II",
            "IS",
            " S"};

    private static final String[] HOE = new String[]{
            "II",
            " S",
            " S"};

    private static final String[] PICKAXE = new String[]{
            "III",
            " S ",
            " S "};

    private static final String[] SHOVEL = new String[]{
            "I",
            "S",
            "S"};

    private static final String[] SWORD = new String[]{
            "I",
            "I",
            "S"};

    public static void init() {
        plugin.getGoldPanManager().setPanChance(Material.GRAVEL, new ItemStack(Material.FLINT), 1000);
        plugin.getGoldPanManager().setPanChance(Material.GRAVEL, new ItemStack(Material.IRON_NUGGET), 350);
        plugin.getGoldPanManager().setPanChance(Material.GRAVEL, new ItemStack(Material.GOLD_NUGGET), 40);


        //I have no idea what this code means
        //plugin.getGoldPanManager().setPanChance(Material.GOLD_NUGGET, new ItemStack(Material.FLINT), 40);

        addTool(AXE, ROSE_GOLD_AXE, MetalsTypes.ROSE_GOLD_INGOT.create());
        addTool(HOE, ROSE_GOLD_HOE, MetalsTypes.ROSE_GOLD_INGOT.create());
        addTool(PICKAXE, ROSE_GOLD_PICKAXE, MetalsTypes.ROSE_GOLD_INGOT.create());
        addTool(SHOVEL, ROSE_GOLD_SHOVEL, MetalsTypes.ROSE_GOLD_INGOT.create());
        addTool(SWORD, ROSE_GOLD_SWORD, MetalsTypes.ROSE_GOLD_INGOT.create());

        GearManager gm = api.gearManager();

        gm.setController(ROSE_GOLD_AXE, (x) -> 180);
        gm.setController(ROSE_GOLD_HOE, (x) -> 180);
        gm.setController(ROSE_GOLD_PICKAXE, (x) -> 180);
        gm.setController(ROSE_GOLD_SHOVEL, (x) -> 180);
        gm.setController(ROSE_GOLD_SWORD, (x) -> 180);

        gm.setMiningLevel(ROSE_GOLD_PICKAXE, 2);
    }

    private static void addTool(String[] shape, ModItem result, ItemStack ingredient) {
        ShapedRecipe tool = new ShapedRecipe(result.getId(), result.create());

        tool.shape(shape);

        tool.setIngredient('I', ingredient);
        tool.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(tool);
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory BASIC_TOOLS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "basic_tools_category"),
            Material.STICK, Component.text("Basic Tools").color(TextColor.color(0xba9163)).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Basic tools").decoration(TextDecoration.ITALIC, false));

    public static final ModItem GOLD_PAN = item.createBuilder(Material.BOWL, plugin, "gold_pan").displayName("Gold Pan", NamedTextColor.GOLD).category(BASIC_TOOLS).build();
    public static final ModItem BANDAGE = item.createBuilder(Material.PAPER, plugin, "bandage").displayName("Bandage", 0xfff59e).category(BASIC_TOOLS).build();

    public static final ModItem ROSE_GOLD_AXE = item.createBuilder(Material.GOLDEN_AXE, plugin, "rose_gold_axe").displayName("Rose Gold Axe", 0xeffd2b3).category(BASIC_TOOLS).build();
    public static final ModItem ROSE_GOLD_HOE = item.createBuilder(Material.GOLDEN_HOE, plugin, "rose_gold_hoe").displayName("Rose Gold Hoe", 0xeffd2b3).category(BASIC_TOOLS).build();
    public static final ModItem ROSE_GOLD_PICKAXE = item.createBuilder(Material.GOLDEN_PICKAXE, plugin, "rose_gold_pickaxe").displayName("Rose Gold Pickaxe", 0xeffd2b3).category(BASIC_TOOLS).build();
    public static final ModItem ROSE_GOLD_SHOVEL = item.createBuilder(Material.GOLDEN_SHOVEL, plugin, "rose_gold_shovel").displayName("Rose Gold Shovel", 0xeffd2b3).category(BASIC_TOOLS).build();
    public static final ModItem ROSE_GOLD_SWORD = item.createBuilder(Material.GOLDEN_SWORD, plugin, "rose_gold_sword").displayName("Rose Gold Sword", 0xeffd2b3).category(BASIC_TOOLS).build();

    //public static final ModItem RAID_HORN = item.createBuilder(Material.GOAT_HORN, plugin, "raid_horn").displayName("Raid Horn", 0x808080).category(BASIC_TOOLS).build();
}
