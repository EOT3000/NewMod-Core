package me.bergenfly.newmod.flyfun.food;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class AnimalsTypes {
    public static void init() {

    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory ANIMALS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "animals_category"),
            Material.BEEF, Component.text("Animals and Meat").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Meat").decoration(TextDecoration.ITALIC, false));

    public static final ModItem SMALL_SALMON =  item.createBuilder(Material.SALMON, plugin, "small_salmon").displayName("Raw Small Salmon", 0xF5A68C).category(ANIMALS).build();
    public static final ModItem MEDIUM_SALMON = item.createBuilder(Material.SALMON, plugin, "medium_salmon").displayName("Raw Medium Salmon", 0xD86B52).category(ANIMALS).build();
    public static final ModItem GIANT_SALMON  = item.createBuilder(Material.SALMON, plugin, "giant_salmon").displayName("Raw Giant Salmon", 0xA8231E).category(ANIMALS).build();

    public static final ModItem GOAT  = item.createBuilder(Material.SALMON, plugin, "goat").displayName("Raw Goat", 0xD5BCA6).category(ANIMALS).build();

}
