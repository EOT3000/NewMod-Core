package me.bergenfly.newmod.core.api.item.builder.meta;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * A modifier for items. Adds meta such as lore, name, or color.
 */
public interface MetaModifier {
    /**
     * Applies this modifier to the provided item.
     *
     * @param stack the item to modify.
     */
    void apply(ItemStack stack);

    MetaModifier GLOW = stack -> {
        if(stack.getType().equals(Material.MACE)) {
            stack.addEnchant(Enchantment.PROTECTION, 1, true);
        } else {
            stack.addEnchant(Enchantment.BREACH, 1, true);
        }

        stack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    };
}
