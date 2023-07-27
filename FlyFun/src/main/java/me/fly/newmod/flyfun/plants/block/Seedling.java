package me.fly.newmod.flyfun.plants.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.block.data.ModBlockData;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public class Seedling implements ModBlock {
    private final String id;

    public Seedling(String id) {
        this.id = id;
    }

    @Override
    public NamespacedKey getId() {
        return new NamespacedKey(FlyFunPlugin.get(), id);
    }

    @Override
    public Material getMaterial() {
        return Material.MELON_STEM;
    }

    @Override
    public Class<? extends ModBlockData> getDataType() {
        return null;
    }

    @Override
    public boolean place(Block block, ModBlockInstance instance) {
        return true;
    }

    @Override
    public void tick(int tick, Block block, ModBlockInstance instance) {

    }
}
