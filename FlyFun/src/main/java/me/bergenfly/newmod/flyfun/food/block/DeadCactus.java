package me.bergenfly.newmod.flyfun.food.block;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public class DeadCactus implements ModBlock {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;

    public static final NamespacedKey DEAD_CACTUS = new NamespacedKey("newmod-core", "dead_cactus");

    @Override
    public NamespacedKey getId() {
        return new NamespacedKey(plugin, "dead_cactus");
    }

    @Override
    public Material getMaterial() {
        return Material.CACTUS;
    }


    @Override
    public void place0(Block block) {
        api.blockStorage().getBlock(block.getLocation()).setData(DEAD_CACTUS, "yes", BlockStorage.StorageType.BLOCK_DATA);
    }
}
