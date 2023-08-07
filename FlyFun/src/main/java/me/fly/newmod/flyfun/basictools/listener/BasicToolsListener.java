package me.fly.newmod.flyfun.basictools.listener;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.basictools.BasicToolsTypes;
import me.fly.newmod.flyfun.basictools.GoldPanManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Random;

public class BasicToolsListener implements Listener {
    private final Random random = new Random();
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final ItemManager item = api.itemManager();
    private static final GoldPanManager goldPan = plugin.getGoldPanManager();

    public static final NamespacedKey BANDAGE_DAMAGE = new NamespacedKey(plugin, "bandage_damage");
    public static final NamespacedKey VITAMIN_DAMAGE = new NamespacedKey(plugin, "vitamin_damage");

    public static final NamespacedKey BANDAGING = new NamespacedKey(plugin, "bandaging");

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ModItem modItem = item.getType(event.getItem());

        if(BasicToolsTypes.GOLD_PAN.equals(modItem)) {
            if(event.hasBlock()) {
                Material t = event.getClickedBlock().getType();
                Location l = event.getClickedBlock().getLocation();

                Map<ItemStack, Integer> possibleItems = goldPan.getPossibleItems(t);

                int max = goldPan.getTotalValue(t);

                if(max == 0) {
                    return;
                }

                event.getClickedBlock().setType(Material.AIR);

                int needed = random.nextInt(max);
                int current = 0;

                for(ItemStack stack : possibleItems.keySet()) {
                    if((current+=goldPan.getPanChance(t, stack)) >= needed) {
                        l.getWorld().dropItem(l, stack);
                        return;
                    }
                }
            }
        } else if(BasicToolsTypes.BANDAGE.equals(modItem)) {
            PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();

            if (pdc.getOrDefault(BANDAGING, PersistentDataType.INTEGER, 0) == 0) {
                if(pdc.getOrDefault(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, 0.0) >= 2) {
                    event.getPlayer().sendMessage(Component.text("Started bandaging. Don't move.").color(NamedTextColor.GREEN));

                    pdc.set(BANDAGING, PersistentDataType.INTEGER, 30);
                } else {
                    event.getPlayer().sendMessage(Component.text("Nowhere to bandage.").color(NamedTextColor.RED));
                }
            } else {
                //TODO: messages
                event.getPlayer().sendMessage(Component.text("Already bandaging!").color(NamedTextColor.RED));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent edbee) {
            if(sharp(edbee.getDamager())) {
                double bdc = event.getEntity().getPersistentDataContainer().getOrDefault(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, 0.0);

                event.getEntity().getPersistentDataContainer().set(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, bdc+event.getDamage()/2);
            }
        }
    }

    private boolean sharp(Entity damager) {
        if(damager instanceof Mob mob) {
            Material material = mob.getEquipment().getItemInMainHand().getType();
            String n = material.name().toLowerCase();

            if(n.contains("_sword") || n.contains("_axe")) {
                return true;
            }
        }

        if(damager instanceof Player mob) {
            Material material = mob.getEquipment().getItemInMainHand().getType();
            String n = material.name().toLowerCase();

            if(n.contains("_sword") || n.contains("_axe")) {
                return true;
            }
        }

        if(damager instanceof AbstractArrow) {
            return true;
        }

        return false;
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            PersistentDataContainer pdc = player.getPersistentDataContainer();
            int c = pdc.getOrDefault(BANDAGING, PersistentDataType.INTEGER, 0);

            if(c > 0) {
                c--;
                pdc.set(BANDAGING, PersistentDataType.INTEGER, c);

                if(c % 5 == 0) {
                    if(c == 0) {
                        PlayerInventory inv = player.getInventory();

                        if(BasicToolsTypes.BANDAGE.equals(item.getType(inv.getItemInMainHand()))) {
                            ItemStack itemStack = inv.getItemInMainHand();

                            if(itemStack.getAmount() > 1) {
                                itemStack.setAmount(itemStack.getAmount()-1);
                            } else {
                                inv.setItemInMainHand(null);
                            }
                        } else if(BasicToolsTypes.BANDAGE.equals(item.getType(inv.getItemInOffHand()))) {
                            ItemStack itemStack = inv.getItemInOffHand();

                            if(itemStack.getAmount() > 1) {
                                itemStack.setAmount(itemStack.getAmount()-1);
                            } else {
                                inv.setItemInOffHand(null);
                            }
                        } else {
                            return;
                        }

                        player.sendMessage(Component.text("The bandage has been wrapped.").color(NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(Component.text("Wrapping...").color(NamedTextColor.YELLOW));
                    }
                }
            }
        }
    }
}
