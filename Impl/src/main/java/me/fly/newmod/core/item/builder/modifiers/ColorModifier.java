package me.fly.newmod.core.item.builder.modifiers;

import me.fly.newmod.core.api.item.builder.meta.MetaModifier;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public final class ColorModifier implements MetaModifier {
    private final Color color;

    public ColorModifier(Color color) {
        this.color = color;
    }

    public ColorModifier(int color) {
        this(Color.fromRGB(color));
    }

    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        if(meta instanceof LeatherArmorMeta cm) {
            cm.setColor(color);
        }

        if(meta instanceof PotionMeta cm) {
            cm.setColor(color);
        }

        stack.setItemMeta(meta);

        //TODO: fireworks
    }
}
