package me.fly.newmod.core.api.crafting;

import org.bukkit.NamespacedKey;

/**
 * Manages disabled and enabled crafting recipes.
 */
public interface CraftingManager {
    /**
     * Sets a recipe to be disabled, or removes recipe being disabled.
     *
     * @param key the recipe key to be disabled or not disabled.
     * @param disabled true if recipe should be disabled, false if not.
     */
    void setDisabled(NamespacedKey key, boolean disabled);

    /**
     * Checks if a recipe is disabled.
     *
     * @param key the recipe to check.
     * @return true if disabled, false if not. A recipe not being disabled does not necessarily mean the recipe exists.
     */
    boolean isDisabled(NamespacedKey key);
}
