package me.bergenfly.newmod.core.item.category;

import me.bergenfly.newmod.core.api.item.builder.modifiers.DisplayNameModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.LoreModifier;
import me.bergenfly.newmod.core.api.item.category.CategoryManager;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CategoryManagerImpl implements CategoryManager {
    private final Map<NamespacedKey, ModItemCategory> categories = new LinkedHashMap<>();

    @Override
    public ModItemCategory createCategory(NamespacedKey id, Material cover, TextComponent displayName, TextComponent... lore) {
        ItemStack stack = new ItemStack(cover);

        new DisplayNameModifier(displayName).apply(stack);
        new LoreModifier(Arrays.asList(lore)).apply(stack);

        ModItemCategory cat = new ModItemCategoryImpl(stack, id);

        categories.put(id, cat);

        return cat;
    }

    @Override
    public ModItemCategory createCategory(NamespacedKey id, ItemStack stack) {
        return categories.put(id, new ModItemCategoryImpl(stack, id));
    }

    @Override
    public ModItemCategory getCategory(NamespacedKey key) {
        return categories.get(key);
    }

    @Override
    public Set<ModItemCategory> getCategories() {
        return new LinkedHashSet<>(categories.values());
    }
}
