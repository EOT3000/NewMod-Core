package me.fly.newmod.flyfun.magic.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages altar recipes.
 *
 * @see me.fly.newmod.flyfun.magic.block.altar.AncientPedestal
 */
public class AltarRecipeManager {
    private Map<NamespacedKey, AltarRecipe> recipes = new HashMap<>();

    /**
     * Adds an altar recipe.
     *
     * @param recipe the recipe.
     */
    public void addRecipe(AltarRecipe recipe) {
        recipes.put(recipe.getKey(), recipe);
    }

    /**
     * Gets the recipes by their center-top and center item.
     *
     * @param head the center-top item of the recipe.
     * @param center the center item of the recipe.
     * @return the recipes with a matching head and center.
     */
    public Set<AltarRecipe> getRecipes(ItemStack head, ItemStack center) {
        Set<AltarRecipe> set = new HashSet<>();
        for(AltarRecipe recipe : recipes.values()) {
            if(head.isSimilar(recipe.getRecipe()[1]) && center.isSimilar(recipe.getRecipe()[0])) {
                set.add(recipe);
            }
        }

        return set;
    }
}
