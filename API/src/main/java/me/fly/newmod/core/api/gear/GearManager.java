package me.fly.newmod.core.api.gear;

import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.inventory.ItemStack;

/**
 * Manages custom durability and armor.
 */
public interface GearManager {

    /**
     * Returns the maximum durability of this item.
     *
     * @param stack the item to check.
     * @return the maximum durability of the provided item; -1 if the item has no durability or is unbreakable.
     */
    int getMaxDurability(ItemStack stack);

    /**
     * Sets the maximum durability of this item.
     *
     * @param stack the item to set the maximum durability of.
     * @param durability the maximum durability to set.
     */
    void setMaxDurability(ItemStack stack, int durability);

    /**
     * Returns the current damage of this item.
     *
     * @param stack the stack to check.
     * @return the current damage of the provided item; -1 if the item has no durability or is unbreakable.
     */
    int getDamage(ItemStack stack);

    /**
     * Sets this item's damage.
     *
     * @param stack the item to set damage for.
     * @param damage the amount of damage to set.
     */
    void setDamage(ItemStack stack, int damage);

    /**
     * Returns the durability controller for this item type.
     *
     * @param item the item type.
     * @return the durability controller for this item type.
     */
    DurabilityController getController(ModItem item);

    /**
     * Sets an item controller for an item.
     *
     * @param item the item.
     * @param controller the controller.
     */
    void setController(ModItem item, DurabilityController controller);



    /**
     * The four armor sections.
     */
    enum ArmorSection {
        HELMET(0.3f),
        CHESTPLATE(0.3f),
        LEGGINGS(0.25f),
        BOOTS(0.15f);

        public final float modifier;

        ArmorSection(float modifier) {
            this.modifier = modifier;
        }
    }

}
