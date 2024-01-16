package me.fly.newmod.flyfun.magic;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.block.BasicBlock;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.block.altar.AncientPedestal;
import me.fly.newmod.flyfun.magic.block.spawner.RepairedSpawner;
import me.fly.newmod.flyfun.magic.recipe.AltarRecipe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import static org.bukkit.Material.*;

@SuppressWarnings("unused")
public class MagicTypes {
    public static void init() {
        plugin.getAltarRecipeManager().addRecipe(new AltarRecipe(SOUL_SWORD.create(), plugin, "soul_sword")
                .setRecipe(is(NETHERITE_SWORD), is(SOUL_SAND), SOUL_JAR.create(), is(SOUL_SAND), SOUL_JAR.create(), is(NETHER_STAR), SOUL_JAR.create(), is(SOUL_SAND), SOUL_JAR.create()));

        plugin.getAltarRecipeManager().addRecipe(new AltarRecipe(SOUL_SHOVEL.create(), plugin, "soul_shovel")
                .setRecipe(is(NETHERITE_SHOVEL), is(WITHER_SKELETON_SKULL), SOUL_JAR.create(), is(BLAZE_POWDER), SOUL_JAR.create(), is(NETHER_STAR), SOUL_JAR.create(), is(BLAZE_POWDER), SOUL_JAR.create()));

        plugin.getAltarRecipeManager().addRecipe(new AltarRecipe(SOUL_NUGGET.create(), plugin, "soul_nugget")
                .setRecipe(is(HONEYCOMB), is(BLAZE_POWDER), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create(), FILLED_SOUL_JAR.create()));

        plugin.getAltarRecipeManager().addRecipe(new AltarRecipe(SOUL_INGOT.create(), plugin, "soul_ingot")
                .setRecipe(is(HONEYCOMB), is(BLAZE_POWDER), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create(), SOUL_NUGGET.create()));

        Bukkit.addRecipe(new ShapelessRecipe(SOUL_JAR.getId(), SOUL_JAR.create())
                .addIngredient(GLASS_BOTTLE)
                .addIngredient(HONEYCOMB)
                .addIngredient(CLAY)
                .addIngredient(BLAZE_POWDER));
    }

    private static ItemStack spwnr(EntityType type) {
        return RepairedSpawner.itemOfType(type);
    }

    private static ItemStack is(Material material) {
        return new ItemStack(material);
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory MAGIC = api.categoryManager().createCategory(new NamespacedKey(plugin, "magic_category"),
            BLAZE_POWDER, Component.text("Magic").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Magical items").decoration(TextDecoration.ITALIC, false));

    public static final ModItem ANCIENT_PEDESTAL = item.createBuilder(DISPENSER, plugin, "ancient_pedestal").block(new AncientPedestal()).displayName("Ancient Pedestal", NamedTextColor.GRAY)
            .addLore(Component.text("")).addLore(Component.text("Originally Created by Slimefun Devs").decoration(TextDecoration.ITALIC, false)).category(MAGIC).build();

    public static final ModItem ANCIENT_ALTAR = item.createBuilder(ENCHANTING_TABLE, plugin, "ancient_altar").block(new BasicBlock(plugin, "ancient_altar", ENCHANTING_TABLE)).displayName("Ancient Altar", NamedTextColor.DARK_PURPLE)
            .addLore(Component.text("")).addLore(Component.text("Originally Created by Slimefun Devs").decoration(TextDecoration.ITALIC, false)).category(MAGIC).build();

    public static final ModItem SOUL_JAR = item.createBuilder(GLASS_BOTTLE, plugin, "soul_jar").displayName("Soul Jar", 0xdfcf3e).category(MAGIC).build();

    public static final ModItem FILLED_SOUL_JAR = item.createBuilder(EXPERIENCE_BOTTLE, plugin, "filled_soul_jar").displayName("Filled Soul Jar", 0xdfcf3e).category(MAGIC).build();

    public static final ModItem SOUL_SWORD = item.createBuilder(NETHERITE_SWORD, plugin, "soul_sword").displayName("Soul Sword", 0x502860).category(MAGIC).build();
    public static final ModItem SOUL_SHOVEL = item.createBuilder(NETHERITE_SHOVEL, plugin, "soul_shovel").displayName("Soul Shovel", 0x502860).category(MAGIC).build();

    public static final ModItem SOUL_NUGGET = item.createBuilder(GOLD_NUGGET, plugin, "soul_nugget").displayName("Soul Nugget", 0xdfcf3e).category(MAGIC).build();
    public static final ModItem SOUL_INGOT = item.createBuilder(GOLD_INGOT, plugin, "soul_ingot").displayName("Soul Ingot", 0xdfcf3e).category(MAGIC).build();

    public static final ModItem BROKEN_SPAWNER = item.createBuilder(SPAWNER, plugin, "broken_spawner").displayName("Broken Spawner", 0x8da2c4).category(MAGIC).build();

    public static final ModItem REPAIRED_SPAWNER = item.createBuilder(SPAWNER, plugin, "repaired_spawner").displayName("Repaired Spawner", 0x8da2c4).category(MAGIC).build();

}
