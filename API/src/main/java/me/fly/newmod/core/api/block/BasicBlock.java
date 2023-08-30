package me.fly.newmod.core.api.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A basic block for storage, without functionality.
 */
public final class BasicBlock implements ModBlock {
    private final NamespacedKey id;
    private final Material material;

    public BasicBlock(NamespacedKey id, Material material) {
        this.id = id;
        this.material = material;
    }

    public BasicBlock(JavaPlugin plugin, String id, Material material) {
        this.id = new NamespacedKey(plugin, id);
        this.material = material;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
