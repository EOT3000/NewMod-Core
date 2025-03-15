package me.bergenfly.newmod.flyfun.food;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.food.nutrient.ModFoodBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class AnimalsTypes {
    public static void init() {
        ItemStack rabbitChunks = RABBIT_CHUNKS.create();

        rabbitChunks.setAmount(2);

        Bukkit.addRecipe(new ShapelessRecipe(RABBIT_CHUNKS.getId(), rabbitChunks).addIngredient(Material.COOKED_RABBIT));

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(plugin, "rabbit_stew_from_red_mushroom"), new ItemStack(Material.RABBIT_STEW))
                .addIngredient(RABBIT_CHUNKS.create())
                .addIngredient(Material.RED_MUSHROOM)
                .addIngredient(Material.BAKED_POTATO)
                .addIngredient(Material.CARROT)
                .addIngredient(Material.BOWL));


        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(plugin, "rabbit_stew_from_green_mushroom"), new ItemStack(Material.RABBIT_STEW))
                .addIngredient(Material.STICK)
                .addIngredient(Material.RED_MUSHROOM)
                .addIngredient(Material.BAKED_POTATO)
                .addIngredient(Material.CARROT)
                .addIngredient(Material.BOWL));

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(plugin, "rabbit_stew_from_brown_mushroom"), new ItemStack(Material.RABBIT_STEW))
                .addIngredient(RABBIT_CHUNKS.create())
                .addIngredient(Material.BROWN_MUSHROOM)
                .addIngredient(Material.BAKED_POTATO)
                .addIngredient(Material.CARROT)
                .addIngredient(Material.BOWL));
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory ANIMALS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "animals_category"),
            Material.BEEF, Component.text("Animals and Meat").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Meat").decoration(TextDecoration.ITALIC, false));

    public static final ModItem SMALL_SALMON =  new ModFoodBuilder(Material.SALMON, plugin, "small_salmon").displayName("Raw Small Salmon", 0xF5A68C).category(ANIMALS)
            .food(1, 0.4f, 2, 3).build();
    public static final ModItem MEDIUM_SALMON = new ModFoodBuilder(Material.SALMON, plugin, "medium_salmon").displayName("Raw Medium Salmon", 0xD86B52).category(ANIMALS)
            .food(2, 1.4f, 4, 3).build();
    public static final ModItem GIANT_SALMON = new ModFoodBuilder(Material.SALMON, plugin, "giant_salmon").displayName("Raw Giant Salmon", 0xA8231E).category(ANIMALS)
            .food(4, 3.8f, 7, 3).build();

    public static final ModItem COOKED_SMALL_SALMON = new ModFoodBuilder(Material.COOKED_SALMON, plugin, "cooked_small_salmon").displayName("Cooked Small Salmon", 0xF5A68C).category(ANIMALS)
            .food(3, 3.8f, 2, 2).build();
    public static final ModItem COOKED_MEDIUM_SALMON = new ModFoodBuilder(Material.COOKED_SALMON, plugin, "cooked_medium_salmon").displayName("Cooked Medium Salmon", 0xD86B52).category(ANIMALS)
            .food(7, 7.2f, 4, 2).build();
    public static final ModItem COOKED_GIANT_SALMON = new ModFoodBuilder(Material.COOKED_SALMON, plugin, "cooked_giant_salmon").displayName("Cooked Giant Salmon", 0xA8231E).category(ANIMALS)
            .food(11, 14.0f, 7, 2).build();

    public static final ModItem GOAT = new ModFoodBuilder(Material.MUTTON, plugin, "goat").displayName("Raw Goat", 0xD5BCA6).category(ANIMALS)
            .food(2, 1.0f, 5, 3).build();

    public static final ModItem COOKED_GOAT = new ModFoodBuilder(Material.COOKED_MUTTON, plugin, "cooked_goat").displayName("Cooked Goat", 0x7F654B).category(ANIMALS)
            .food(5, 8.2f, 4, 2).build();

    public static final ModItem RABBIT_CHUNKS = new ModFoodBuilder(Material.BEETROOT_SEEDS, plugin, "cooked_rabbit_chunks").displayName("Cooked Rabbit Chunks", 0x7F654B).category(ANIMALS)
            .food(3, 3.0f, 2, 1).build();

}
