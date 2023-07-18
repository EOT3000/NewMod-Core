package me.fly.newmod.core.item.category;

import me.fly.newmod.core.api.item.builder.modifiers.DisplayNameModifier;
import me.fly.newmod.core.api.item.builder.modifiers.LoreModifier;
import me.fly.newmod.core.api.item.category.CategoryManager;
import me.fly.newmod.core.api.item.category.ModItemCategory;
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

        return categories.put(id, new ModItemCategoryImpl(stack, id));
    }

    @Override
    public ModItemCategory createCategory(NamespacedKey id, ItemStack stack) {
        return categories.put(id, new ModItemCategoryImpl(stack, id));
    }

    @Override
    public Set<ModItemCategory> getCategories() {
        return new LinkedHashSet<>(categories.values());
    }
}
