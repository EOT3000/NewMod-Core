package me.fly.newmod.core.api.item;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * A class which represents a custom mod item.
 */
public interface ModItem extends Item {
    /**
     * @return the id of this item.
     */
    NamespacedKey getId();

    /**
     * @return the material of this item.
     */
    Material getMaterial();

    /**
     * @return the display name of this item.
     */
    Component getDisplayName();

    /**
     * @return the data type of this item.
     */
    Class<? extends ModItemData> getDataType();

    /**
     * @return this item's block, or null if it has none.
     */
    ModBlock getBlock();

    /**
     * Applies the defined item modifiers to the provided item stack.
     *
     * @param stack the stack to apply to.
     * @return the stack.
     */
    ItemStack applyModifiers(ItemStack stack);


}
