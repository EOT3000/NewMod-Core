package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.manager.NationsPermissionManager;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a permission to do some action in a {@link Lot}.
 * <p>
 * Default plot permissions are found at {@link DefaultPlotPermission}. Additional permissions may be defined by other plugins, and registered using {@link NationsPermissionManager#register()}
 */
public interface PlotPermission {

    /**
     * The id of this permission.
     *
     * @return this permission's id.
     */
    @NotNull NamespacedKey getKey();

    @NotNull String getName();
}
