package me.fly.newmod.core.listener;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.util.PersistentDataUtil;
import me.fly.newmod.core.command.CheatCommand;
import me.fly.newmod.core.item.ItemManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class CheatInventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
            return;
        }

        if(event.getCurrentItem().getPersistentDataContainer().getOrDefault(CheatCommand.INVENTORY, PersistentDataType.STRING, "").equals("cheat")) {
            event.setCancelled(true);

            NamespacedKey key = event.getCurrentItem().getPersistentDataContainer().get(ItemManagerImpl.ID, PersistentDataUtil.NAMESPACED_KEY);

            if(key == null) {
                return;
            }

            Inventory inventory = Bukkit.createInventory(event.getWhoClicked(), 54, event.getCurrentItem().displayName());

            for(ModItem item : NewModPlugin.get().categoryManager().getCategory(key).getItems()) {
                inventory.addItem(item.create());
            }

            event.getWhoClicked().openInventory(inventory);
        }
    }
}
