package me.bergenfly.newmod.flyfun.fortunefix;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;

public class FortuneListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockDropItemEvent event) {
        if(FortuneDistributions.getDropItem(event.getBlock().getType()) == null) {
            return;
        }

        if(!event.getItems().removeIf(item -> item.getItemStack().getType().equals(FortuneDistributions.getDropItem(event.getBlock().getType())))) {
            return;
        }

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), FortuneDistributions.createDrop(event.getBlock().getType(), event.getPlayer().getInventory().getItemInMainHand().getEnchantLevel(Enchantment.FORTUNE)));
    }
}
