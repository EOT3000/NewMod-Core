package me.bergenfly.newmod.core.listener;

import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.NewModPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDurabilityWaste(PlayerItemDamageEvent event) {
        int changeDura = event.getDamage();

        GearManager gm = NewModPlugin.get().gearManager();
        gm.setDamage(event.getItem(), gm.getDamage(event.getItem())+changeDura);
    }

    // Crafting durability found in VanillaReplacementListener
}
