package me.fly.newmod.core.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public interface ModItem extends Item {
    NamespacedKey getId();

    Material getMaterial();
}
