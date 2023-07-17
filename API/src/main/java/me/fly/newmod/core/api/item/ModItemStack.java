package me.fly.newmod.core.api.item;

import me.fly.newmod.core.api.item.data.ModItemData;

public interface ModItemStack {
    /**
     * Gets the amount of items in this stack.
     *
     * @return the amount of items.
     */
    int getAmount();

    /**
     * Sets the amount of items in this stack.
     *
     * @param amount the amount of items.
     */
    void setAmount(int amount);

    /**
     * Gets this mod item stack's item type.
     *
     * @return the item type.
     */
    ModItem getType();

    /**
     * Gets this item stack's data.
     *
     * @return the item data.
     */
    ModItemData getData();

    /**
     * Sets this item stack's data.
     *
     * @param data the item data.
     */
    void setData(ModItemData data);

    /**
     * Updates the item stack. Called after modifying the item data.
     */
    void update();
}

