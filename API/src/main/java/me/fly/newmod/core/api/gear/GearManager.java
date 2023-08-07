package me.fly.newmod.core.api.gear;

import org.bukkit.inventory.ItemStack;

/**
 * Manages custom durability and armor.
 */
public interface GearManager {

    /**
     * Returns the maximum durability of this item.
     *
     * @param stack the stack to check.
     * @return the maximum durability of the provided item; -1 if the item has no durability or is unbreakable.
     */
    int getMaxDurability(ItemStack stack);

    /**
     * Returns the current durability of this item.
     *
     * @param stack the stack to check.
     * @return the current durability of the provided item; -1 if the item has no durability or is unbreakable.
     */
    int getDurability(ItemStack stack);

    /**
     * The four armor sections.
     */
    enum ArmorSection {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

}
