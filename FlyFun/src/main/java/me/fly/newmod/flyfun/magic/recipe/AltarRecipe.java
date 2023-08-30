package me.fly.newmod.flyfun.magic.recipe;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AltarRecipe implements Recipe, Keyed {
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
