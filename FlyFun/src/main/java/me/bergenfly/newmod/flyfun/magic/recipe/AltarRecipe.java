package me.bergenfly.newmod.flyfun.magic.recipe;

import me.bergenfly.newmod.core.api.item.ModItem;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * An altar recipe.
 *
 * @see AltarRecipeManager
 */

//TODO redo this whole class
public class AltarRecipe implements Recipe, Keyed {
    private static final String[] RECIPE_ORDER = {"MIDDLE", "VERTEX", "CORNER", "VERTEX", "CORNER", "VERTEX", "CORNER", "VERTEX", "CORNER"};

    private final ItemStack result;
    private final NamespacedKey key;
    private ItemStack[] ingredients;

    public AltarRecipe(ItemStack result, NamespacedKey key) {
        this.result = result;
        this.key = key;
    }

    public AltarRecipe(ItemStack result, JavaPlugin plugin, String key) {
        this(result, new NamespacedKey(plugin, key));
    }

    public AltarRecipe(ModItem item) {
        this(item.create(), item.getId());
    }

    public AltarRecipe(ModItem item, int count) {
        this(item.create().asQuantity(count), item.getId());
    }

    /**
     * Sets the recipe ingredients. The order is like so:
     *
     * 812
     * 703
     * 654
     *
     * @param ingredients the ingredients to set to.
     * @return this recipe.
     */
    public AltarRecipe setRecipe(ItemStack... ingredients) {
        this.ingredients = ingredients;

        return this;
    }

    public ItemStack[] getRecipe() {
        return ingredients.clone();
    }

    @Override
    public @NotNull ItemStack getResult() {
        return result;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
