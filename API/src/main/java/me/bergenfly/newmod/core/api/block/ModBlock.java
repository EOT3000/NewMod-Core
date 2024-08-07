package me.bergenfly.newmod.core.api.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
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
     * Checks if this block should be placed given this event, where a player placed the item linked to this block.
     *
     * @param event the place event.
     * @return true if the block should be placed here, false if not.
     */
    default boolean shouldPlace(BlockPlaceEvent event) {
        return true;
    }

    /**
     * Sets the given block.
     *
     * @param block the block to place at.
     * @param manager the manager to use.
     */
    default void setType(Block block, BlockManager manager) {
        manager.setBlock(block, this);
    }

    /**
     * Places this block, without an event.
     *
     * @param block the block to place at.
     */
    default void place(Block block) {
        if(block.getType() != getMaterial()) {
            block.setType(getMaterial());
        }

        place0(block);
    }

    /**
     * Places this block, with an event.
     *
     * @param event the event.
     */
    default void place(BlockPlaceEvent event) {
        place(event.getBlock());
    }

    /**
     * Sets the final block data necessary to place the block.
     *
     * @param block the block to place at.
     */
    default void place0(Block block) {

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

    /**
     * Ticks the block.
     *
     * @param block the block to tick.
     */
    default void tick(Block block) {

    }
}
