package me.fly.newmod.core.api.block;

import me.fly.newmod.core.api.block.data.ModBlockData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

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
     * @return the data type of this block.
     */
    Class<? extends ModBlockData> getDataType();

    /**
     * Places this block.
     *
     * @param block the block to place at.
     * @param instance the container which holds the data the block should be placed with. May be null.
     * @return whether or not the block was placed successfully. If true, data should be added to the block.
     */
    boolean place(Block block, ModBlockInstance instance);

    /**
     * Ticks the block. This method should only be used by ticking listeners.
     *
     * @param tick the tick number.
     * @param block the block to tick.
     * @param instance data holder.
     */
    void tick(int tick, Block block, ModBlockInstance instance);

    /**
     * Checks whether this block should be deleted or not.
     *
     * @param block the block to check.
     * @return whether the block should be removed, kept, or should this block use the default checking method?
     */
    default Event.Result shouldDelete(Block block) {
        return Event.Result.DEFAULT;
    }
}
