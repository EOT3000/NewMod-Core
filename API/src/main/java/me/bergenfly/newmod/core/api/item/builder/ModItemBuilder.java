package me.bergenfly.newmod.core.api.item.builder;

import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;

/**
 * A mod item builder. Creates and registers a mod item with the provided properties.
 */
public interface ModItemBuilder {
    /**
     * Sets the display name.
     *
     * @param component the custom name to set.
     */
    ModItemBuilder displayName(TextComponent component);

    /**
     * Sets the display name.
     *
     * @param string the text to use.
     * @param color the color to use.
     */
    default ModItemBuilder displayName(String string, TextColor color) {
        return displayName(Component.text(string).color(color).decoration(TextDecoration.ITALIC, false));
    }

    /**
     * Sets the display name.
     *
     * @param string the text to use.
     * @param color the color to use.
     */
    default ModItemBuilder displayName(String string, int color) {
        return displayName(string, TextColor.color(color));
    }

    /**
     * Sets the item color.
     *
     * @param color the color to set.
     */
    ModItemBuilder color(Color color);

    /**
     * Sets the item color.
     *
     * @param color the color to set.
     */
    default ModItemBuilder color(int color) {
        return color(Color.fromRGB(color));
    }

    /**
     * Adds an enchantment.
     *
     * @param enchantment the enchantment.
     * @param lvl the level.
     */
    ModItemBuilder addEnchantment(Enchantment enchantment, int lvl);

    /**
     * Adds a line of lore.
     *
     * @param component the line to add.
     */
    ModItemBuilder addLore(TextComponent component);

    /**
     * Adds an additional modifier.
     *
     * @param modifier the modifier to apply.
     */
    ModItemBuilder addModifier(MetaModifier modifier);



    /**
     * Sets this item's block.
     *
     * @param block the block to set.
     */
    ModItemBuilder block(ModBlock block);

    /**
     * Sets this item's data type.
     *
     * @param clazz the class of the data type to set.
     */
    ModItemBuilder dataType(Class<? extends ModItemData> clazz);



    /**
     * @param category sets the item category.
     */
    ModItemBuilder category(ModItemCategory category);



    /**
     * Builds and registers the item.
     *
     * @return the created item.
     */
    ModItem build();
}
