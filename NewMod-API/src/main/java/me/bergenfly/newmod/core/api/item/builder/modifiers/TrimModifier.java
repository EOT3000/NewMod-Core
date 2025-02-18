package me.bergenfly.newmod.core.api.item.builder.modifiers;

import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public record TrimModifier(TrimMaterial material, TrimPattern pattern) implements MetaModifier {
    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        if(meta instanceof ArmorMeta a) {
            a.setTrim(new ArmorTrim(material, pattern));
        }

        stack.setItemMeta(meta);
    }
}
