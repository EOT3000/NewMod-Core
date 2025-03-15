package me.bergenfly.newmod.flyfun.food.block;

import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Seedling implements ModBlock {
    private final String id;
    private final Consumer<Block> growFunction;
    public ItemStack drop;

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
    public boolean shouldPlace(BlockPlaceEvent event) {
        return event.getBlock().getRelative(0,-1,0).getType().equals(Material.FARMLAND);
    }

    @Override
    public void place(Block block) {
        block.setType(Material.MELON_STEM);
    }

    public void grow(Block t) {
        growFunction.accept(t);
    }

    @Override
    public void tick(int tick, Block block) {

    }
}
