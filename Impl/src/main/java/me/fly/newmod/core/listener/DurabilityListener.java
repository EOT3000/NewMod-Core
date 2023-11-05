package me.fly.newmod.core.listener;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityListener implements Listener {
    @EventHandler
    public void onDurabilityWaste(PlayerItemDamageEvent event) {
        int changeDura = event.getOriginalDamage()-event.getDamage();

        GearManager gm = NewModPlugin.get().gearManager();
        gm.setDamage(event.getItem(), gm.getDamage(event.getItem())-changeDura);
    }
}