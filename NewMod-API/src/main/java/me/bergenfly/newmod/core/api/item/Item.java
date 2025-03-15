package me.bergenfly.newmod.core.api.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Represents an item, either a vanilla {@link org.bukkit.Material}, or a {@link ModItem}.
 */
public interface Item {
    ItemStack create();

    void setIngredient(char c, ShapedRecipe recipe);
}
