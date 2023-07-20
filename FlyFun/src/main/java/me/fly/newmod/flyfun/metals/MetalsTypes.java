package me.fly.newmod.flyfun.metals;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public class MetalsTypes {
    public static void init() {

    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory METALS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "metals_category"),
            Material.IRON_INGOT, Component.text("Metals").color(NamedTextColor.RED).decorate(TextDecoration.BOLD), Component.text(""), Component.text("Metals, ores and refining"));

    public static final ModItem ALUMINUM_INGOT = item.createBuilder(Material.IRON_INGOT, plugin, "aluminum_ingot").displayName("Aluminum Ingot", 0x93adb8).category(METALS).build();
}
