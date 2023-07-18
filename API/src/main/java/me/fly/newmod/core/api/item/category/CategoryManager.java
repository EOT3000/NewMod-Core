package me.fly.newmod.core.api.item.category;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * Manager of item categories.
 */
public interface CategoryManager {
    /**
     * Creates and registers a new item category.
     *
     * @param id the category ID.
     * @param cover the cover item.
     * @param displayName the cover item's display name.
     * @param lore the cover item's lore.
     * @return the new category.
     */
    ModItemCategory createCategory(NamespacedKey id, Material cover, TextComponent displayName, TextComponent... lore);

    /**
     * Creates and registers a new item category.
     *
     * @param id the category ID.
     * @param stack the cover item.
     * @return the new category.
     */
    ModItemCategory createCategory(NamespacedKey id, ItemStack stack);

    /**
     * Gets all registered item categories.
     *
     * @return a set of all item categories.
     */
    Set<ModItemCategory> getCategories();
}
