package me.fly.newmod.core.api.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * A basic block for storage, without functionality.
 */
public final class BasicBlock implements ModBlock {
    private final NamespacedKey id;
    private final Material material;
    private ItemStack drop;

    public BasicBlock(NamespacedKey id, Material material) {
        this.id = id;
        this.material = material;
    }

    public BasicBlock(JavaPlugin plugin, String id, Material material) {
        this.id = new NamespacedKey(plugin, id);
        this.material = material;
    }

    public BasicBlock(NamespacedKey id, Material material, ItemStack drop) {
        this.id = id;
        this.material = material;
        this.drop = drop;
    }

    public BasicBlock(JavaPlugin plugin, String id, Material material, ItemStack drop) {
        this.id = new NamespacedKey(plugin, id);
        this.material = material;
        this.drop = drop;
    }

    public void setDrop(ItemStack drop) {
        this.drop = drop;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public List<ItemStack> getDrops(Block block, Player breaker) {
        return Collections.singletonList(drop);
    }
}
