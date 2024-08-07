package me.fly.newmod.flyfun.basictools.listener;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.basictools.BasicToolsTypes;
import me.fly.newmod.flyfun.basictools.GoldPanManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Random;

//TODO: split for all the tools?
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
            if(event.getHand() != EquipmentSlot.HAND) {
                return;
            }

            PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();

            if (!isBandaging(pdc)) {
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

                        if(noBandage(inv)) {
                            return;
                        }

                        ItemStack bandage = inv.getItemInMainHand();

                        if(bandage.getAmount() > 1) {
                            bandage.setAmount(bandage.getAmount()-1);
                        } else {
                            inv.setItemInMainHand(null);
                        }

                        pdc.set(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, pdc.getOrDefault(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, 1.0)-1);

                        player.setHealth(player.getHealth()+1);

                        player.sendMessage(Component.text("The bandage has been wrapped.").color(NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(Component.text("Wrapping...").color(NamedTextColor.YELLOW));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if(event.getEntity() instanceof Player player) {
            PersistentDataContainer pdc = event.getEntity().getPersistentDataContainer();

            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-player.getHealth()-event.getAmount() < pdc.getOrDefault(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, 0.0)) {
                switch (event.getRegainReason()) {
                    case SATIATED, EATING, MAGIC_REGEN, MAGIC -> event.setAmount(event.getAmount() / 4);
                }

                pdc.set(BANDAGE_DAMAGE, PersistentDataType.DOUBLE, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-player.getHealth()-event.getAmount());
            }
        }
    }

    @EventHandler
    public void onHandSwitch(PlayerSwapHandItemsEvent event) {
        PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();

        if(isBandaging(pdc)) {
            pdc.set(BANDAGING, PersistentDataType.INTEGER, 0);
            event.getPlayer().sendMessage(Component.text("Cancelled bandaging.").color(NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getPlayer().getPersistentDataContainer().set(BANDAGING, PersistentDataType.INTEGER, 0);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        PersistentDataContainer pdc = event.getWhoClicked().getPersistentDataContainer();

        if(isBandaging(pdc)) {
            pdc.set(BANDAGING, PersistentDataType.INTEGER, 0);
            event.getWhoClicked().sendMessage(Component.text("Cancelled bandaging.").color(NamedTextColor.RED));
        }
    }

    private boolean noBandage(PlayerInventory inv) {
        return !BasicToolsTypes.BANDAGE.equals(item.getType(inv.getItemInMainHand()));
    }

    private boolean isBandaging(PersistentDataContainer pdc) {
        return pdc.getOrDefault(BANDAGING, PersistentDataType.INTEGER, 0) > 0;
    }
}
