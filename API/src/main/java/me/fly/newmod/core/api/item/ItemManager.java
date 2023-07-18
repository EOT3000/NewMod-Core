package me.fly.newmod.core.api.item;

import me.fly.newmod.core.api.item.builder.ModItemBuilder;
import me.fly.newmod.core.api.item.data.ModItemData;
import me.fly.newmod.core.api.item.data.ModItemDataSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A singleton which holds the item registry and provides mod item utility methods.
 */
public interface ItemManager {
    /**
     * Registers an item to this manager.
     *
     * @param item the item to register.
     */
    void registerItem(ModItem item);

    /**
     * Attempts to find this ItemStack's ModItem.
     *
     * @param stack the ItemStack to check.
     * @return the found ModItem, or null if none could be found.
     */
    ModItem getType(ItemStack stack);

    /**
     * Attempts to find the ModItem of this NamespacedKey
     *
     * @param key the key.
     * @return the found ModItem, or null if none could be found.
     */
    ModItem getType(NamespacedKey key);

    /**
     * Registers an item data serializer.
     *
     * @param serializer the serializer to register.
     * @param clazz the serializer class.
     */
    <T extends ModItemData> void registerSerializer(ModItemDataSerializer<T> serializer, Class<T> clazz);

    /**
     * Creates the default version of an item data type.
     *
     * @param type the item for which the data should be created.
     * @return the created default data.
     */
    <T extends ModItemData> T createDefaultData(ModItem type);

    /**
     * Gets the data stored on this item stack.
     *
     * @param stack the stack to check.
     * @return the item data, or null if there is none.
     */
    ModItemData getData(ItemStack stack);

    /**
     * Creates a mod item builder.
     *
     * @param material the item material.
     * @param plugin the plugin.
     * @param id the item id.
     * @return the created builder.
     */
    ModItemBuilder createBuilder(Material material, JavaPlugin plugin, String id);

    /**
     * Applies data to an item stack.
     *
     * @param stack the stack to apply the data to.
     * @param data the data to apply.
     * @return true if successful, false if it fails, such as if the data type does not match the stack.
     */
    boolean applyData(ItemStack stack, ModItemData data);
}
