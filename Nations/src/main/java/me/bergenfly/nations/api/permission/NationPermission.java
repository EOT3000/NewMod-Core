package me.bergenfly.nations.api.permission;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface NationPermission {
    /**
     * The id of this permission.
     *
     * @return this permission's id.
     */
    @NotNull NamespacedKey getKey();
}
