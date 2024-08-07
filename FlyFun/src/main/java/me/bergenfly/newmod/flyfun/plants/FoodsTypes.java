package me.bergenfly.newmod.flyfun.plants;

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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class FoodsTypes {
    public static void init() {
        addBreadRecipe(FLOUR, new ItemStack(Material.BREAD), "bread");
        addBreadRecipe(WATTLE_FLOUR, WATTLE_BREAD.create(), "wattle_bread");
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory FOODS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "foods_category"),
            Material.BREAD, Component.text("Foods").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Foodstuffs").decoration(TextDecoration.ITALIC, false));

    public static final ModItem FLOUR = item.createBuilder(Material.SUGAR, plugin, "flour").displayName("Flour", 0xFFFFEF).category(FOODS).build();
    public static final ModItem WATTLE_FLOUR = item.createBuilder(Material.GLOWSTONE_DUST, plugin, "wattle_flour").displayName("Wattle Flour", 0xa88847).category(FOODS).build();

    public static final ModItem WATTLE_BREAD = item.createBuilder(Material.BREAD, plugin, "wattle_bread").displayName("Wattle Bread", 0xa88847).category(FOODS).build();

    private static void addBreadRecipe(ModItem flour, ItemStack result, String key) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, key), result);

        recipe.shape("FFF");

        recipe.setIngredient('F', flour.create());

        Bukkit.addRecipe(recipe);
    }
}