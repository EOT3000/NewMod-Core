package me.fly.newmod.core.api.gear;

import me.fly.newmod.core.api.item.ModItem;

/**
 * Manages custom food.
 */
public interface FoodManager {

    /**
     * Gets if this mod item is a food.
     *
     * @param item the item to check.
     * @return true if this item is a food, false if not.
     */
    boolean isFood(ModItem item);

    /**
     * Gets the hunger points for this mod item.
     *
     * @param item the item to check.
     * @return the hunger points this item should give.
     */
    int getHunger(ModItem item);

    /**
     * Gets the saturation for this mod item.
     *
     * @param item the item to check.
     * @return the saturation this item should give.
     */
    float getSaturation(ModItem item);

    /**
     * Registers a mod food.
     *
     * @param item the item to register.
     * @param hunger the hunger points this item should give.
     * @param saturation the saturation this item should give.
     */
    void registerFood(ModItem item, int hunger, float saturation);
}
