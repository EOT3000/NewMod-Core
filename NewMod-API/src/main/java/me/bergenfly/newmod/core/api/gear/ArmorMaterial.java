package me.bergenfly.newmod.core.api.gear;

import org.bukkit.NamespacedKey;

/**
 * Represents an armor material, such as iron, gold or diamond. May also represent a mod material such as rose gold.
 */
public interface ArmorMaterial {
    /**
     * This material's id.
     * @return the material's id.
     */
    NamespacedKey getId();


}
