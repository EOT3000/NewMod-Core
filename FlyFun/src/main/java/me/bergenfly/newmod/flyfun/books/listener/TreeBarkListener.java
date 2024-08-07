package me.fly.newmod.flyfun.books.listener;

import me.fly.newmod.flyfun.books.BooksTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class TreeBarkListener implements Listener {
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onStrip(PlayerInteractEvent event) {
        //TODO: not this weird way with the contains _AXE, make it right
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getItem().getType().name().contains("_AXE")
                && (event.getClickedBlock().getType().equals(Material.BIRCH_LOG) || event.getClickedBlock().getType().equals(Material.BIRCH_WOOD))) {
            Location drop = event.getInteractionPoint();

            if(random.nextBoolean()) return;

            drop.getWorld().dropItemNaturally(drop, BooksTypes.BIRCH_BARK.create());
        }
    }
}
