package me.fly.newmod.core.api.event;

import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.item.ModItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.function.BiConsumer;

/**
 * Manages events done by mod items or blocks.
 */
public interface EventManager {
    /**
     * Creates a block event handler.
     *
     * @param clazz the event type.
     * @param handler the handler.
     * @param priority the event priority to register.
     * @param ignoreCancelled whether the event should ignore the event if it is cancelled.
     * @return true if the registration was successful, false if not.
     */
    <T extends Event> boolean registerBlockEventHandler(Class<T> clazz, BiConsumer<T, ModBlockInstance> handler, EventPriority priority, boolean ignoreCancelled);

    /**
     * Creates a item event handler.
     *
     * @param clazz the event type.
     * @param handler the handler.
     * @param priority the event priority to register.
     * @param ignoreCancelled whether the event should ignore the event if it is cancelled.
     * @return true if the registration was successful, false if not.
     */
    <T extends Event> boolean registerItemEventHandler(Class<T> clazz, BiConsumer<T, ModItemStack> handler, EventPriority priority, boolean ignoreCancelled);
}
