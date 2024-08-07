package me.bergenfly.newmod.flyfun.books;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.books.data.WritableItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public class BooksTypes {
    public static void init() {
        plugin.getBooksManager().setPages(BIRCH_BARK, 1);
    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory WRITABLE = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "writable_category"),
            Material.WRITABLE_BOOK, Component.text("Writable").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Books, bark and writing").decoration(TextDecoration.ITALIC, false));

    public static final ModItem BIRCH_BARK = item.createBuilder(Material.PAPER, plugin, "birch_bark").dataType(WritableItemData.class).displayName("Birch Bark", 0xeef5c4).category(WRITABLE).build();
}
