package me.fly.newmod.flyfun.metals;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import static org.bukkit.Material.*;
import static org.bukkit.Material.BLAZE_POWDER;

@SuppressWarnings("unused")
public class MetalsTypes {
    public static void init() {
        Bukkit.addRecipe(new ShapelessRecipe(ROSE_GOLD_DUST.getId(), ROSE_GOLD_DUST.create().add(1))
                .addIngredient(GOLD_DUST.create())
                .addIngredient(COPPER_DUST.create()));

        Bukkit.addRecipe(new BlastingRecipe(ROSE_GOLD_INGOT.getId(), ROSE_GOLD_INGOT.create(), new RecipeChoice.ExactChoice(ROSE_GOLD_DUST.create()), 3.0f, 100));
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory METALS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "metals_category"),
            Material.IRON_INGOT, Component.text("Metals").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Metals, ores and refining").decoration(TextDecoration.ITALIC, false));

    public static final ModItem ALUMINUM_INGOT = item.createBuilder(Material.IRON_INGOT, plugin, "aluminum_ingot").displayName("Aluminum Ingot", 0x93adb8).category(METALS).build();

    public static final ModItem COPPER_DUST = item.createBuilder(Material.GLOWSTONE_DUST, plugin, "copper_dust").displayName("Copper Dust", 0xfa7f2d).category(METALS).build();
    public static final ModItem GOLD_DUST = item.createBuilder(Material.GLOWSTONE_DUST, plugin, "gold_dust").displayName("Gold Dust", 0xe6be60).category(METALS).build();
    public static final ModItem ROSE_GOLD_DUST = item.createBuilder(Material.GLOWSTONE_DUST, plugin, "rose_gold_dust").displayName("Rose Gold Dust", 0xeffd2b3).category(METALS).build();

    public static final ModItem ROSE_GOLD_INGOT = item.createBuilder(Material.COPPER_INGOT, plugin, "rose_gold_ingot").displayName("Rose Gold Ingot", 0xeffd2b3).category(METALS).build();
}
