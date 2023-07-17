package me.fly.newmod.core.api.item.builder.modifiers;

import me.fly.newmod.core.api.item.builder.meta.MetaModifier;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class EnchantmentModifier implements MetaModifier {
    private final Enchantment enchantment;
    private final int lvl;

    public EnchantmentModifier(Enchantment enchantment, int lvl) {
        this.enchantment = enchantment;
        this.lvl = lvl;
    }

    @Override
    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        meta.addEnchant(enchantment, lvl, true);

        stack.setItemMeta(meta);
    }
}
