package me.bergenfly.newmod.core.api.item;

import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.api.item.data.ModItemDataSerializer;
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
    ModItem getModType(ItemStack stack);

    /**
     * Attempts to find this ItemStack's Item type, either vanilla or modded.
     *
     * @param stack the ItemStack to check.
     * @return the found Item, or null of none could be found.
     */
    Item getType(ItemStack stack);

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
    <T extends ModItemData> void registerSerializer(ModItemDataSerializer<? extends T> serializer, Class<T> clazz);

    /**
     * Creates a mod item stack from an existing item.
     *
     * @param stack the item to create from.
     * @return the created mod item stack.
     */
    ModItemStack getStack(ItemStack stack);

    /**
     * Creates a mod item stack from a mod item type.
     *
     * @param item the mod item type to create from.
     * @return the created mod item stack.
     */
    ModItemStack getStack(ModItem item);

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
