package me.bergenfly.newmod.core.item.builder;

import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.block.BasicBlock;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.ColorModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.DisplayNameModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.EnchantmentModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.LoreModifier;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public class ModItemBuilderImpl implements ModItemBuilder {
    private final Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    private final List<TextComponent> lore = new ArrayList<>();
    private final List<MetaModifier> modifiers = new ArrayList<>();

    private TextComponent displayName;
    private ModBlock block;
    private Color color;
    private Class<? extends ModItemData> data;

    private final Material material;
    private final NamespacedKey key;

    private ModItemCategory category;

    public ModItemBuilderImpl(Material material, NamespacedKey key) {
        this.material = material;
        this.key = key;
    }

    @Override
    public ModItemBuilder displayName(TextComponent component) {
        this.displayName = component;

        return this;
    }

    @Override
    public ModItemBuilder color(Color color) {
        this.color = color;

        return this;
    }

    @Override
    public ModItemBuilder addEnchantment(Enchantment enchantment, int lvl) {
        enchantments.put(enchantment, lvl);

        return this;
    }

    @Override
    public ModItemBuilder addLore(TextComponent component) {
        lore.add(component);

        return this;
    }

    @Override
    public ModItemBuilder addModifier(MetaModifier modifier) {
        modifiers.add(modifier);

        return this;
    }

    @Override
    public ModItemBuilder block(ModBlock block) {
        this.block = block;

        return this;
    }

    @Override
    public ModItemBuilder dataType(Class<? extends ModItemData> clazz) {
        this.data = clazz;

        return this;
    }

    @Override
    public ModItemBuilder category(ModItemCategory category) {
        this.category = category;

        return this;
    }

    protected ModItem factory(NamespacedKey id, Material material, TextComponent component,
                              ModBlock block, Class<? extends ModItemData> data, List<MetaModifier> modifiers) {
        return new BuiltModItemImpl(id, material, component, block, data, modifiers);
    }

    @Override
    public ModItem build() {
        if(displayName == null) {
            displayName = Component.text("");
        }

        modifiers.add(new DisplayNameModifier(displayName));

        if(color != null) {
            modifiers.add(new ColorModifier(color));
        }

        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            modifiers.add(new EnchantmentModifier(entry.getKey(), entry.getValue()));
        }

        if(lore.size() != 0) {
            modifiers.add(new LoreModifier(lore));
        }

        ModItem item = factory(key, material, displayName, block, data, modifiers);

        if(category != null) {
            category.addItem(item);
        }

        BlockManager bm = NewModPlugin.get().blockManager();

        if(block != null && bm.getType(block.getId()) == null) {
            bm.registerBlock(block);

            if(block instanceof BasicBlock) {
                ((BasicBlock) block).setDrop(item.create());
            }
        }

        NewModPlugin.get().itemManager().registerItem(item);

        System.out.println("Item built: " + key.toString());

        return item;
    }


}
