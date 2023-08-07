package me.fly.newmod.flyfun.basictools;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public class BasicToolsTypes {
    public static void init() {

    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory BASIC_TOOLS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "basic_tools_category"),
            Material.STICK, Component.text("Basic Tools").color(TextColor.color(0xba9163)).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Basic tools").decoration(TextDecoration.ITALIC, false));

    public static final ModItem GOLD_PAN = item.createBuilder(Material.BOWL, plugin, "gold_pan").displayName("Gold Pan", NamedTextColor.GOLD).category(BASIC_TOOLS).build();
    public static final ModItem BANDAGE = item.createBuilder(Material.PAPER, plugin, "bandage").displayName("Bandage", 0xfff59e).category(BASIC_TOOLS).build();
}
