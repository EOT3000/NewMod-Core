package me.bergenfly.newmod.core.api.item.builder.modifiers;

import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public record ColorModifier(Color color) implements MetaModifier {
    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        if (meta instanceof LeatherArmorMeta cm) {
            cm.setColor(color);
        }

        if (meta instanceof PotionMeta cm) {
            cm.setColor(color);
        }

        stack.setItemMeta(meta);

        //TODO: fireworks
    }
}
