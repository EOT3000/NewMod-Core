package me.bergenfly.newmod.core.api.item.builder.modifiers;

import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record LoreModifier(List<TextComponent> lore) implements MetaModifier {
    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        meta.lore(lore);

        stack.setItemMeta(meta);
    }
}
