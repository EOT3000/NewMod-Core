package me.bergenfly.newmod.core.api.gear;

import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.item.Item;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArmorSetBuilder {
    ArmorSetBuilder material(Item item);

    ArmorSetBuilder definePiece(@NotNull GearManager.ArmorSection section, Material material, String id, int armor, int toughness, int durability);

    ArmorSetBuilder trim(@Nullable GearManager.ArmorSection section, TrimMaterial material, TrimPattern pattern);

    /**
     * Sets the display name.
     *
     * @param section the section to set for, or null to set for all.
     * @param component the custom name to set.
     */
    ArmorSetBuilder displayName(@Nullable GearManager.ArmorSection section, TextComponent component);

    /**
     * Sets the display name.
     *
     * @param section the section to set for, or null to set for all.
     * @param string the text to use.
     * @param color the color to use.
     */
    default ArmorSetBuilder displayName(@Nullable GearManager.ArmorSection section, String string, TextColor color) {
        return displayName(section, Component.text(string).color(color).decoration(TextDecoration.ITALIC, false));
    }

    /**
     * Sets the display name.
     *
     * @param section the section to set for, or null to set for all.
     * @param string the text to use.
     * @param color the color to use.
     */
    default ArmorSetBuilder displayName(@Nullable GearManager.ArmorSection section, String string, int color) {
        return displayName(section, string, TextColor.color(color));
    }

    /**
     * Sets the item color.
     *
     * @param section the section to set for, or null to set for all.
     * @param color the color to set.
     */
    ArmorSetBuilder color(@Nullable GearManager.ArmorSection section, Color color);

    /**
     * Sets the item color.
     *
     * @param section the section to set for, or null to set for all.
     * @param color the color to set.
     */
    default ArmorSetBuilder color(@Nullable GearManager.ArmorSection section, int color) {
        return color(section, Color.fromRGB(color));
    }

    /**
     * Adds an enchantment.
     *
     * @param section the section to set for, or null to set for all.
     * @param enchantment the enchantment.
     * @param lvl the level.
     */
    ArmorSetBuilder addEnchantment(@Nullable GearManager.ArmorSection section, Enchantment enchantment, int lvl);

    /**
     * Adds a line of lore.
     *
     * @param section the section to set for, or null to set for all.
     * @param component the line to add.
     */
    ArmorSetBuilder addLore(@Nullable GearManager.ArmorSection section, TextComponent component);

    /**
     * Adds an additional modifier.
     *
     * @param section the section to set for, or null to set for all.
     * @param modifier the modifier to apply.
     */
    ArmorSetBuilder addModifier(@Nullable GearManager.ArmorSection section, MetaModifier modifier);



    /**
     * @param category sets the item category.
     */
    ArmorSetBuilder category(ModItemCategory category);



    /**
     * Builds and registers the set.
     *
     * @return the created set.
     */
    ArmorSet build();
}
