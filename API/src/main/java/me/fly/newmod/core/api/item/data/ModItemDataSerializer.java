package me.fly.newmod.core.api.item.data;

import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.inventory.ItemStack;

/**
 * A class that converts between in-game NBT and code for mod item data.
 *
 * @param <T> the data type this serializer can convert.
 */
public interface ModItemDataSerializer<T extends ModItemData> {
    /**
     * Gets the data stored on this item stack.
     *
     * @param stack the stack to check.
     * @return the item data, or null if there is none.
     */
    T getData(ItemStack stack);

    /**
     * Creates the default version of an item data type.
     *
     * @param type the item for which the data should be created.
     * @return the created default data.
     */
    T createDefaultData(ModItem type);

    /**
     * Applies data to an item stack.
     *
     * @param stack the stack to apply the data to.
     * @param data the data to apply.
     */
    void applyData(ItemStack stack, ModItemData data);

    /**
     * Checks if this serializer can serialize the provided data.
     *
     * @param data the data to check.
     * @return true if this data can be serialized by this serializer, false if it cannot.
     */
    boolean canSerialize(ModItemData data);
}
