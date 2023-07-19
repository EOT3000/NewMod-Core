package me.fly.newmod.core.api.item.category;

import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * An item category. These will be shown in the guide.
 */
public interface ModItemCategory {
    /**
     * @return a copy of this category's cover item.
     */
    ItemStack getCover();

    /**
     * @return this category's id.
     */
    NamespacedKey getId();

    /**
     * @return a set of the items this category contains.
     */
    Set<ModItem> getItems();

    /**
     * Adds an item to this category.
     *
     * @param item the item to add.
     */
    void addItem(ModItem item);
}
