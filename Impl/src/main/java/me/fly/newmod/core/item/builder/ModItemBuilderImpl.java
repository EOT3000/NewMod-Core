package me.fly.newmod.core.item.builder;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.builder.ModItemBuilder;
import me.fly.newmod.core.api.item.builder.meta.MetaModifier;
import me.fly.newmod.core.api.item.data.ModItemData;
import me.fly.newmod.core.api.util.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class ModItemBuilderImpl implements ModItemBuilder {
    private Color color;
    private final List<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();
    private final List<TextComponent> lore = new ArrayList<>();

    TextComponent customName;
    ModBlock block;

    public ModItemBuilderImpl() {

    }

    @Override
    public ModItemBuilder displayName(TextComponent component) {
        return this;
    }

    @Override
    public ModItemBuilder color(int color) {
        return null;
    }

    @Override
    public ModItemBuilder addEnchantment(Enchantment enchantment, int lvl) {
        enchantments.add(new Pair<>(enchantment, lvl));

        return this;
    }

    @Override
    public ModItemBuilder addLore(TextComponent component) {
        lore.add(component);

        return this;
    }

    @Override
    public ModItemBuilder addModifier(MetaModifier modifier) {
        return null;
    }

    @Override
    public ModItemBuilder setBlock(ModBlock block) {
        return null;
    }

    @Override
    public ModItemBuilder setDataType(Class<? extends ModItemData> clazz) {
        return null;
    }

    @Override
    public ModItem build() {
        return null;
    }
}
