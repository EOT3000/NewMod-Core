package me.fly.newmod.core.api.gear;

import org.bukkit.inventory.ItemStack;

/**
 * Manages the durability for crafted custom items.
 */
public interface DurabilityController {
    /**
     * Called whenever a registered item is crafted. The return value of this method will be the maximum durability for the item.
     *
     * @param stack the crafted item.
     * @return the max durability the item shoudl have.
     */
    int getMaxDurabilityForCraft(ItemStack stack);
}
