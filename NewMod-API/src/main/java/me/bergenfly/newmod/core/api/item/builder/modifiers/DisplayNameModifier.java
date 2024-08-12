package me.bergenfly.newmod.core.api.item.builder.modifiers;

import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record DisplayNameModifier(TextComponent component) implements MetaModifier {
    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        meta.displayName(component);

        stack.setItemMeta(meta);
    }
}
