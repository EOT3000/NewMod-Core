package me.fly.newmod.core.listener;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MiningLevelListener implements Listener {
    private static final NewModPlugin api = NewModPlugin.get();
    private static final ItemManager item = api.itemManager();
    private static final GearManager gear = api.gearManager();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreakLowest(BlockBreakEvent event) {
        ItemStack main = event.getPlayer().getInventory().getItemInMainHand();
        ModItem type = item.getType(main);

        if(type != null && gear.getMiningLevel(type) == 2) {
            if(Tag.NEEDS_STONE_TOOL.isTagged(event.getBlock().getType())
                    || Tag.NEEDS_IRON_TOOL.isTagged(event.getBlock().getType())) {
                event.setDropItems(true);
            }
        }
    }

    //TODO: generalize to all items, not jsut golden pickaxes.

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakMonitor(BlockBreakEvent event) {
        ItemStack main = event.getPlayer().getInventory().getItemInMainHand();
        ModItem type = item.getType(main);

        if(type != null && gear.getMiningLevel(type) == 2) {
            if((Tag.NEEDS_STONE_TOOL.isTagged(event.getBlock().getType())
                    || Tag.NEEDS_IRON_TOOL.isTagged(event.getBlock().getType()))
                    && main.getType().equals(Material.GOLDEN_PICKAXE)) {
                ItemStack fake = main.clone();
                fake.setType(Material.IRON_PICKAXE);

                //Will this work?
                //event.setDropItems(true);
                //It did not

                if(event.isDropItems()) {
                    List<Item> list = new ArrayList<>();

                    for (ItemStack stack : event.getBlock().getDrops(fake, event.getPlayer())) {
                        list.add(event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack));
                    }

                    BlockDropItemEvent dropItemEvent = new BlockDropItemEvent(event.getBlock(), event.getBlock().getState(), event.getPlayer(), new ArrayList<>(list));

                    Bukkit.getPluginManager().callEvent(dropItemEvent);

                    for(Item i : list) {
                        if(!dropItemEvent.getItems().contains(i)) {
                            i.remove();
                        }
                    }

                    //Should not modify events in MONITOR, however it is necessary to prevent two events from firing.
                    event.setDropItems(false);
                }
            }
        }
    }
}
