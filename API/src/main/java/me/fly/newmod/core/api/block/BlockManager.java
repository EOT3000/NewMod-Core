package me.fly.newmod.core.api.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.data.ModBlockData;
import me.fly.newmod.core.api.block.data.ModBlockDataSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlockStack;

/**
 * A singleton which holds the block registry and provides mod block utility methods.
 */
public interface BlockManager {
    /**
     * Registers an block to this manager.
     *
     * @param block the block to register.
     */
    void registerBlock(ModBlock block);

    /**
     * Attempts to find this BlockStack's ModBlock.
     *
     * @param stack the BlockStack to check.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(BlockStack stack);

    /**
     * Attempts to find the ModBlock of this NamespacedKey
     *
     * @param key the key.
     * @return the found ModBlock, or null if none could be found.
     */
    ModBlock getType(NamespacedKey key);

    /**
     * Registers an block data serializer.
     *
     * @param serializer the serializer to register.
     * @param clazz the serializer class.
     */
    <T extends ModBlockData> void registerSerializer(ModBlockDataSerializer<T> serializer, Class<T> clazz);

    /**
     * Creates the default version of an block data type.
     *
     * @param type the block for which the data should be created.
     * @return the created default data.
     */
    <T extends ModBlockData> T createDefaultData(ModBlock type);

    /**
     * Gets the data stored on this block stack.
     *
     * @param stack the stack to check.
     * @return the block data, or null if there is none.
     */
    ModBlockData getData(BlockStack stack);

    /**
     * Applies data to an block stack.
     *
     * @param stack the stack to apply the data to.
     * @param data the data to apply.
     * @return true if successful, false if it fails, such as if the data type does not match the stack.
     */
    boolean applyData(BlockStack stack, ModBlockData data);
}
