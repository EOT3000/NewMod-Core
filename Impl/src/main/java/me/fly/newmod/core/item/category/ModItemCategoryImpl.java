package me.fly.newmod.core.item.category;

import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;
import java.util.Set;

public class ModItemCategoryImpl implements ModItemCategory {
    private final Set<ModItem> items = new LinkedHashSet<>();
    private final ItemStack cover;
    private final NamespacedKey id;

    public ModItemCategoryImpl(ItemStack cover, NamespacedKey id) {
        this.cover = cover;
        this.id = id;
    }

    @Override
    public ItemStack getCover() {
        return cover;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Set<ModItem> getItems() {
        return new LinkedHashSet<>(items);
    }

    @Override
    public void addItem(ModItem item) {
        items.add(item);
    }
}
