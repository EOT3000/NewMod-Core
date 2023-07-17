package me.fly.newmod.core.api.item;

import me.fly.newmod.core.api.item.data.ModItemData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 * A class which represents a custom mod item.
 */
public interface ModItem extends Item {
    /**
     * @return the id of this item.
     */
    NamespacedKey getId();

    /**
     * @return the material of this item.
     */
    Material getMaterial();

    /**
     * @return the data type of this item.
     */
    Class<? extends ModItemData> getDataType();
}
