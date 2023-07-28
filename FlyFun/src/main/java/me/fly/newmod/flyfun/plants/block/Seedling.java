package me.fly.newmod.flyfun.plants.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.block.data.ModBlockData;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

import java.util.function.Consumer;

public class Seedling implements ModBlock {
    private final String id;
    private final Consumer<Block> growFunction;

    public Seedling(String id, Consumer<Block> growFunction) {
        this.id = id;
        this.growFunction = growFunction;
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
        if(block.getType().equals(Material.MELON_STEM)) {
            return true;
        }

        block.setType(Material.MELON_STEM);

        return true;
    }

    @Override
    public void tick(int tick, Block block, ModBlockInstance instance) {
        if(!block.getType().equals(Material.MELON_STEM)) {
            return;
        }

        Ageable ageable = (Ageable) block.getBlockData();

        if(ageable.getAge() == 7) {
            growFunction.accept(block);
        }
    }
}
