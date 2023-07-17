package me.fly.newmod.core.item;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.util.PersistentDataUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManagerImpl implements ItemManager {
    public static final NamespacedKey ID = new NamespacedKey(NewModPlugin.get(), "id");

    private final Map<NamespacedKey, ModItem> registry = new LinkedHashMap<>();

    @Override
    public void register(ModItem item) {
        registry.put(item.getId(), item);
    }

    @Override
    public ModItem getType(ItemStack stack) {
        if(stack == null || !stack.hasItemMeta()) {
            return null;
        }

        NamespacedKey id = stack.getItemMeta().getPersistentDataContainer().get(ID, PersistentDataUtil.NAMESPACED_KEY);

        return registry.get(id);
    }

    @Override
    public ModItem getType(NamespacedKey key) {
        return registry.get(key);
    }
}
