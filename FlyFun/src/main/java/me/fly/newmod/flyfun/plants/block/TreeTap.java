package me.fly.newmod.flyfun.plants.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public class TreeTap implements ModBlock {

    @Override
    public NamespacedKey getId() {
        return new NamespacedKey(FlyFunPlugin.get(), "tree_tap");
    }

    @Override
    public Material getMaterial() {
        return Material.TRIPWIRE_HOOK;
    }

    @Override
    public void place(Block block) {
        //nothing
    }
}
