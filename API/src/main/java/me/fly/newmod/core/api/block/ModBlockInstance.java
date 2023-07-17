package me.fly.newmod.core.api.block;

import me.fly.newmod.core.api.block.data.ModBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface ModBlockInstance {

    /**
     * If this instance {@link ModBlockInstance#exists}, and location refers to the existing block, updates the block, and returns the underlying block.
     * Otherwise, creates a block at the provided location, and returns that block.
     *
     * @param location the location to create or get the block from.
     * @return the created or gotten block.
     */
    Block createOrGet(Location location);

    /**
     * Gets if this block exists in-game.
     *
     * @return true if this block exists, false if it doesn't.
     */
    boolean exists();

    /**
     * Gets this mod block type.
     *
     * @return the mod type of this block.
     */
    ModBlock getType();

    /**
     * Gets the data stored in this block.
     *
     * @return the block's data.
     */
    ModBlockData getData();

    /**
     *
     * @param data
     */
    void setData(ModBlockData data);

    void update();
}
