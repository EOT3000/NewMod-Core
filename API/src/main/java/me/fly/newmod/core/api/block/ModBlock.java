package me.fly.newmod.core.api.block;

import me.fly.newmod.core.api.block.data.ModBlockData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public interface ModBlock {
    /**
     * @return the id of this block.
     */
    NamespacedKey getId();

    /**
     * @return the material of this block.
     */
    Material getMaterial();

    /**
     * @return the display name of this block.
     */
    Component getDisplayName();

    /**
     * @return the data type of this block.
     */
    Class<? extends ModBlockData> getDataType();

    /**
     * Places this block.
     *
     * @param block the block to place at.
     * @param instance the container which holds the data the block should be placed with.
     */
    void place(Block block, ModBlockInstance instance);

    /**
     * Checks whether this block should be deleted or not.
     *
     * @param block the block to check.
     * @param instance the block's data.
     * @return true if the block should be deleted, false if it shouldn't.
     */
    boolean shouldDelete(Block block, ModBlockInstance instance);
}
