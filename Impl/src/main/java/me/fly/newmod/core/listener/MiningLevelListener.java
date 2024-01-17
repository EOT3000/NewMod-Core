package me.fly.newmod.core.listener;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MiningLevelListener implements Listener {
    private static final NewModPlugin api = NewModPlugin.get();
    private static final ItemManager item = api.itemManager();
    private static final GearManager gear = api.gearManager();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack main = event.getPlayer().getInventory().getItemInMainHand();
        ModItem type = item.getType(main);

        if(type != null && gear.getMiningLevel(type) == 2) {
            if(Tag.NEEDS_STONE_TOOL.isTagged(event.getBlock().getType())
                    || Tag.NEEDS_IRON_TOOL.isTagged(event.getBlock().getType())) {
                ItemStack fake = main.clone();
                fake.setType(Material.IRON_PICKAXE);

                //Will this work?
                //event.setDropItems(true);
                //It did not

                for(ItemStack stack : event.getBlock().getDrops()) {
                    //TODO: replace with drop item naturally for all instances after I figure out what it does
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
                }
            }
        }
    }
}
