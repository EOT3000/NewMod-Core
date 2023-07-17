package me.fly.newmod.core.api.item.builder;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.item.builder.meta.MetaModifier;
import me.fly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.enchantments.Enchantment;

/**
 * A mod item builder. Creates and registers a mod item with the provided properties.
 */
public interface ModItemBuilder {
    /**
     * Colors the item.
     *
     * @param color the color to use.
     */
    void color(int color);

    /**
     * Sets the custom name.
     *
     * @param component the custom name
     */
    void customName(TextComponent component);

    /**
     * Sets the custom name.
     *
     * @param string the text to use.
     * @param color the color to use.
     */
    void customName(String string, int color);

    /**
     * Adds an enchantment.
     *
     * @param enchantment the enchantment.
     * @param lvl the level.
     */
    void enchantment(Enchantment enchantment, int lvl);

    /**
     * Adds a line of lore.
     *
     * @param component the line to add.
     */
    void addLore(TextComponent component);

    /**
     * Adds an additional modifier.
     *
     * @param modifier the modifier to apply.
     */
    void addModifier(MetaModifier modifier);



    /**
     * Sets this item's block.
     *
     * @param block the block to set.
     */
    void setBlock(ModBlock block);

    /**
     * Sets this item's data type.
     *
     * @param clazz the class of the data type to set.
     */
    void setDataType(Class<? extends ModItemData> clazz);
}
