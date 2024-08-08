package me.bergenfly.nations.api.permission;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public enum DefaultPlotPermission implements PlotPermission {
    BUILD;



    private final Set<Function<PlotPermissionContext, Event.Result>> overrideFunctions = new HashSet<>();

    /**
     * Sets an override function for a plugin.
     *
     * @param predicate the predicate used
     */
    public void setPluginOverrideFunction(JavaPlugin plugin, Function<PlotPermissionContext, Event.Result> predicate) {
        plugin.getPluginMeta().getPluginSoftDependencies();
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return null;
    }
}
