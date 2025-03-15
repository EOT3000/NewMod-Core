package me.bergenfly.newmod.flyfun.magic.listener;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.magic.MagicTypes;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class SoulToolListener implements Listener {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player) {
            LivingEntity e = (LivingEntity) event.getEntity();
            Player d = (Player) event.getDamager();

            if (e.getHealth() - event.getFinalDamage() <= 0) {
                if(MagicTypes.FILLED_SOUL_JAR.equals(item.getModType(d.getInventory().getItemInOffHand()))) {
                    PlayerInventory inv = d.getInventory();

                    ItemStack itemStack = inv.getItemInOffHand();

                    if(itemStack.getAmount() <= 1) {
                        inv.setItemInOffHand(MagicTypes.FILLED_SOUL_JAR.create());
                    } else {
                        itemStack.setAmount(itemStack.getAmount()-1);
                        inv.setItemInOffHand(itemStack);

                        Map<Integer, ItemStack> map = inv.addItem(MagicTypes.FILLED_SOUL_JAR.create());

                        if(!map.isEmpty()) {
                            d.getWorld().dropItem(d.getLocation(), MagicTypes.FILLED_SOUL_JAR.create());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasBlock()) {
            ItemStack offItem = event.getPlayer().getInventory().getItemInOffHand();
            ItemStack mainItem = event.getPlayer().getInventory().getItemInMainHand();

            if(MagicTypes.FILLED_SOUL_JAR.equals(item.getModType(offItem)) && MagicTypes.SOUL_SHOVEL.equals(item.getModType(mainItem))) {
                Material blockType = event.getClickedBlock().getType();

                if(blockType.equals(Material.SAND)) {
                    event.getClickedBlock().setType(Material.SOUL_SAND);
                } else if(blockType.equals(Material.DIRT)) {
                    event.getClickedBlock().setType(Material.SOUL_SOIL);
                } else return;

                if(offItem.getAmount() <= 1) {
                    event.getPlayer().getInventory().setItemInOffHand(null);
                } else {
                    offItem.setAmount(offItem.getAmount() - 1);
                }
            }
        }
    }
}
