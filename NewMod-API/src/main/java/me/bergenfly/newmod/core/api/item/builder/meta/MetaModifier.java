package me.bergenfly.newmod.core.api.item.builder.meta;

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
}
