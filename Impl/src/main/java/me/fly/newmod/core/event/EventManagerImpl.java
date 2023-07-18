package me.fly.newmod.core.event;

import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.event.EventManager;
import me.fly.newmod.core.api.item.ModItemStack;
import me.fly.newmod.core.api.util.Triple;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class EventManagerImpl implements EventManager, Listener {
    private final Map<EventPriority, List<Triple<Class<? extends Event>, BiConsumer<? extends Event, ModBlockInstance>, Boolean>>> blocks = new HashMap<>();
    private final Map<EventPriority, List<Triple<Class<? extends Event>, BiConsumer<? extends Event, ModItemStack>, Boolean>>> items = new HashMap<>();

    @Override
    public <T extends Event> boolean registerBlockEventHandler(Class<T> clazz, BiConsumer<T, ModBlockInstance> handler, EventPriority priority, boolean ignoreCancelled) {
        if(clazz == null || handler == null || priority == null) {
            return false;
        }

        if(clazz.isAssignableFrom(BlockBreakEvent.class)) {
            //blocks.put()
        }

        return false;
    }

    @Override
    public <T extends Event> boolean registerItemEventHandler(Class<T> clazz, BiConsumer<T, ModItemStack> handler, EventPriority priority, boolean ignoreCancelled) {
        if(clazz == null || handler == null || priority == null) {
            return false;
        }

        return false;
    }
}
