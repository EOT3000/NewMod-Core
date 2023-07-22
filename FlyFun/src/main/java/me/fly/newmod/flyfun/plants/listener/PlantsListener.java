package me.fly.newmod.flyfun.plants.listener;

import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.flyfun.plants.PlantsTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlantsListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();

        if(hand.getType().equals(Material.SHEARS) || hand.hasEnchant(Enchantment.SILK_TOUCH)) {
            return;
        }

        if(random.nextDouble() > 0.05) {
            return;
        }

        switch (event.getBlock().getType()) {
            case ACACIA_LEAVES -> dropItem(event.getBlock().getLocation(), PlantsTypes.ACACIA_SEEDS);
            case CHERRY_LEAVES -> dropItem(event.getBlock().getLocation(), PlantsTypes.RED_CHERRIES);
        }
    }

    private void dropItem(Location block, ModItem item) {
        block.getWorld().dropItem(block, item.create());
    }

}
