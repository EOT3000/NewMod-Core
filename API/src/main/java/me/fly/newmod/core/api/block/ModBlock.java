package me.fly.newmod.core.api.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
     * Places this block.
     *
     * @param block the block to place at.
     * @return whether or not the block was placed successfully. If true, data should be added to the block.
     */
    default boolean place(Block block) {
        return true;
    }

    /**
     * Ticks the block. This method should only be invoked by ticking listeners.
     *
     * @param tick the tick number.
     * @param block the block to tick.
     */
    default void tick(int tick, Block block) {

    }

    /**
     * Checks whether this block should be deleted or not.
     *
     * @param block the block to check.
     * @return whether the block should be removed (DENY), kept (ALLOW), or should this block use the default checking method (DEFAULT)?
     */
    default Event.Result shouldDelete(Block block) {
        return Event.Result.DEFAULT;
    }

    /**
     * Gets the drops for this block.
     *
     * @param block the location this block was broken.
     * @param breaker the player who broke this block. This may be null.
     * @return a list of the drops that should be dropped when this block is broken.
     */
    default List<ItemStack> getDrops(Block block, Player breaker) {
        return new ArrayList<>();
    }
}
