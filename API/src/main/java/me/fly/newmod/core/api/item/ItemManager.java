package me.fly.newmod.core.api.item;

import org.bukkit.inventory.ItemStack;

/**
 * A singleton which holds the item registry and provides mod item utility methods.
 */
public interface ItemManager {
    /**
     * Registers an item to this manager.
     *
     * @param item the item to register.
     */
    void register(ModItem item);

    /**
     * Attempts to find this ItemStack's ModItem.
     *
     * @param stack the ItemStack to check.
     * @return the found ModItem, or null if none could be found.
     */
    ModItem getType(ItemStack stack);
}
