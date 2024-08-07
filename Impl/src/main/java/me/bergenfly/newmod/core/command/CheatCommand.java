package me.bergenfly.newmod.core.command;

import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.NewModPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CheatCommand {
    public static final NamespacedKey INVENTORY = new NamespacedKey(NewModPlugin.get(), "inventory");

    public void run(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, Component.text("FlyFun Menu"));

        for(ModItemCategory category : NewModPlugin.get().categoryManager().getCategories()) {
            ItemStack item = category.getCover();
            ItemMeta meta = item.getItemMeta();

            //TODO: namespaced key?
            meta.getPersistentDataContainer().set(INVENTORY, PersistentDataType.STRING, "cheat");

            item.setItemMeta(meta);

            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }
}
