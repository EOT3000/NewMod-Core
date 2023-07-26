package me.fly.newmod.flyfun.books.listener;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.ModItemStack;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.books.data.WritableItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

public class BooksUtils {
    public static final NamespacedKey OFFHAND_ONLY = new NamespacedKey(FlyFunPlugin.get(), "offhand_only");

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    public static void finishWrite(PlayerInventory inventory, BookMeta newBook) {
        Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
            inventory.setItemInOffHand(null);
        }, 1);

        ModItemStack stack = item.getStack(inventory.getItemInMainHand());
        WritableItemData meta = (WritableItemData) stack.getData();

        meta.setText(new String[] {((TextComponent) newBook.page(1)).content()});
        meta.setSigned(true);

        stack.setData(meta);
        stack.update();
    }

    public static ItemStack finishWriteAdd(PlayerInventory inventory, BookMeta newBook) {
        Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
            inventory.setItemInOffHand(null);
        }, 1);

        ItemStack stack = new ItemStack(inventory.getItemInMainHand());

        stack.setAmount(1);

        ModItemStack mod = item.getStack(stack);
        WritableItemData meta = (WritableItemData) mod.getData();

        meta.setText(new String[] {((TextComponent) newBook.page(1)).content()});
        meta.setSigned(true);

        mod.setData(meta);
        mod.update();

        return stack;
    }

    public static boolean writable(ItemStack stack) {
        if(stack == null || !stack.hasItemMeta()) {
            return false;
        }

        ModItem type = item.getType(stack);

        if(type != null && plugin.getBooksManager().writable(type)) {
            return !((WritableItemData) item.getStack(stack).getData()).isSigned();
        }

        return false;
    }

    public static boolean isBook(ItemStack stack) {
        if(stack == null || !stack.hasItemMeta()) {
            return false;
        }

        return (stack.getType().equals(Material.WRITABLE_BOOK) || stack.getType().equals(Material.WRITTEN_BOOK)) &&
                stack.getItemMeta().getPersistentDataContainer().getOrDefault(OFFHAND_ONLY, PersistentDataType.BOOLEAN, false);
    }

    public static void putBook(PlayerInventory inventory) {
        Material type = inventory.getItemInOffHand().getType();

        if(type.equals(Material.AIR) || isBook(inventory.getItemInOffHand())) {
            ItemStack writableBook = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta meta = (BookMeta) writableBook.getItemMeta();

            meta.pages(Component.text(""),
                    Component.text("Writing on this or subsequent pages will not be saved. Only write on page 1.").color(TextColor.color(0xFF0000)));
            meta.getPersistentDataContainer().set(OFFHAND_ONLY, PersistentDataType.BOOLEAN, true);

            writableBook.setItemMeta(meta);

            inventory.setItemInOffHand(writableBook);
        }
    }

    public static boolean signed(ItemStack stack) {
        if(!writable(stack)) {
            return false;
        }

        return ((WritableItemData) item.getStack(stack).getData()).isSigned();
    }
}
