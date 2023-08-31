package me.fly.newmod.flyfun.magic;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.block.BasicBlock;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.block.altar.AncientPedestal;
import me.fly.newmod.flyfun.magic.recipe.AltarRecipe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

@SuppressWarnings("unused")
public class MagicTypes {
    public static void init() {
        plugin.getAltarRecipeManager().addRecipe(new AltarRecipe(SOUL_SWORD.create(), plugin, "ancient_pedestal").
                setRecipe(is(NETHERITE_SWORD), is(SOUL_SAND), is(SAND), is(SOUL_SAND), is(SAND), is(NETHER_STAR), is(SAND), is(SOUL_SAND), is(SAND)));
    }

    private static ItemStack is(Material material) {
        return new ItemStack(material);
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory MAGIC = api.categoryManager().createCategory(new NamespacedKey(plugin, "magic_category"),
            Material.BLAZE_POWDER, Component.text("Magic").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Magical items").decoration(TextDecoration.ITALIC, false));

    public static final ModItem ANCIENT_PEDESTAL = item.createBuilder(Material.DISPENSER, plugin, "ancient_pedestal").block(new AncientPedestal()).displayName("Ancient Pedestal", NamedTextColor.GRAY).category(MAGIC).build();

    public static final ModItem ANCIENT_ALTAR = item.createBuilder(Material.ENCHANTING_TABLE, plugin, "ancient_altar").block(new BasicBlock(plugin, "ancient_altar", Material.ENCHANTING_TABLE)).displayName("Ancient Altar", NamedTextColor.DARK_PURPLE).category(MAGIC).build();

    public static final ModItem SOUL_SWORD = item.createBuilder(Material.NETHERITE_SWORD, plugin, "soul_sword").displayName("Soul Sword", 0x502860).category(MAGIC).build();
}
