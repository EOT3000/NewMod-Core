package me.bergenfly.newmod.flyfun.food.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VanillaPlantListener implements Listener {

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getBlocks());
        System.out.println(event.getLocation());
        System.out.println(event.getSpecies());
        System.out.println(event.isFromBonemeal());
        System.out.println();
        System.out.println();
    }

    @EventHandler
    public void onPlantGrow(BlockGrowEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getNewState().getBlockData());
        System.out.println(event.getBlock());
        System.out.println();
        System.out.println();
    }

    @EventHandler
    public void onPlantGrow(BlockSpreadEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getNewState().getBlockData());
        System.out.println(event.getBlock());
        System.out.println();
        System.out.println();
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getBlocks());
        System.out.println(event.getBlock());
        System.out.println(event.getPlayer());
        System.out.println();
        System.out.println();
    }
}
