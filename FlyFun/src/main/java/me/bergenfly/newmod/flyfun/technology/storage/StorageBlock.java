package me.bergenfly.newmod.flyfun.technology.storage;

import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.technology.EnergyComponent;
import me.bergenfly.newmod.flyfun.technology.util.EnergyUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class StorageBlock implements ModBlock, EnergyComponent {
    private final int capacity;
    private final Material type;

    private final NamespacedKey id;

    private static final BlockManager bm = FlyFunPlugin.get().api.blockManager();

    public StorageBlock(Material type, int capacity, JavaPlugin plugin, String id) {
        this.id = new NamespacedKey(plugin, id);

        this.type = type;
        this.capacity = capacity;
    }

    @Override
    public EnergyComponentType getType() {
        return EnergyComponentType.STORAGE;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Block getDestination(Block from, Block to, int stack) {
        return to;
    }

    @Override
    public int push(Block from, Block to, int energy) {
        int remainingCapacity = capacity-EnergyUtil.getCharge(to.getLocation());

        if(remainingCapacity <= 0) {
            return 0;
        }

        if(remainingCapacity >= energy) {
            EnergyUtil.addCharge(to.getLocation(), energy, capacity);
            return energy;
        }

        EnergyUtil.addCharge(to.getLocation(), remainingCapacity, capacity);

        return remainingCapacity;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return type;
    }
}
