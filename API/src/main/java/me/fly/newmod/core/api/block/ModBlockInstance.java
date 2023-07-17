package me.fly.newmod.core.api.block;

import me.fly.newmod.core.api.block.data.ModBlockData;

public interface ModBlockInstance {


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
