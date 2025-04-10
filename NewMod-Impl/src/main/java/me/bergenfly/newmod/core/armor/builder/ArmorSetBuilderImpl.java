package me.bergenfly.newmod.core.armor.builder;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.newmod.core.api.gear.ArmorSet;
import me.bergenfly.newmod.core.api.gear.ArmorSetBuilder;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.Item;
import me.bergenfly.newmod.core.api.item.ModArmor;
import me.bergenfly.newmod.core.api.item.VanillaItem;
import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.TrimModifier;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.armor.ArmorSetImpl;
import me.bergenfly.newmod.core.item.builder.ModItemBuilderImpl;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ArmorSetBuilderImpl implements ArmorSetBuilder {
    private final Object2IntMap<GearManager.ArmorSection> armor = new Object2IntOpenHashMap<>();
    private final Object2IntMap<GearManager.ArmorSection> toughness = new Object2IntOpenHashMap<>();
    private final Object2IntMap<GearManager.ArmorSection> durability = new Object2IntOpenHashMap<>();
    private final Map<GearManager.ArmorSection, ArmorItemBuilder> builders = new EnumMap<>(GearManager.ArmorSection.class);

    private final NamespacedKey id;

    private Item material;

    public ArmorSetBuilderImpl(NamespacedKey key) {
        this.id = key;
    }

    @Override
    public ArmorSetBuilder material(Item item) {
        this.material = item;

        return this;
    }

    @Override
    public ArmorSetBuilder definePiece(GearManager.@NotNull ArmorSection section, Material material, String id, int armor, int toughness, int durability) {
        this.armor.put(section, armor);
        this.toughness.put(section, toughness);
        this.durability.put(section, durability);
        this.builders.put(section, new ArmorItemBuilder(material, new NamespacedKey(this.id.getNamespace(), id), section, armor, toughness, durability));

        return this;
    }

    @Override
    public ArmorSetBuilder trim(GearManager.@Nullable ArmorSection section, TrimMaterial material, TrimPattern pattern) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).addModifier(new TrimModifier(material, pattern));
            }
        } else {
            builders.get(section).addModifier(new TrimModifier(material, pattern));
        }

        return this;
    }

    @Override
    public ArmorSetBuilder displayName(GearManager.@Nullable ArmorSection section, TextComponent component) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).displayName(component);
            }
        } else {
            builders.get(section).displayName(component);
        }

        return this;
    }

    @Override
    public ArmorSetBuilder color(GearManager.@Nullable ArmorSection section, Color color) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).color(color);
            }
        } else {
            builders.get(section).color(color);
        }

        return this;
    }

    @Override
    public ArmorSetBuilder addEnchantment(GearManager.@Nullable ArmorSection section, Enchantment enchantment, int lvl) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).addEnchantment(enchantment, lvl);
            }
        } else {
            builders.get(section).addEnchantment(enchantment, lvl);
        }

        return this;
    }

    @Override
    public ArmorSetBuilder addLore(GearManager.@Nullable ArmorSection section, TextComponent component) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).addLore(component);
            }
        } else {
            builders.get(section).addLore(component);
        }

        return this;
    }

    @Override
    public ArmorSetBuilder addModifier(GearManager.@Nullable ArmorSection section, MetaModifier modifier) {
        if(section == null) {
            for(GearManager.ArmorSection s : builders.keySet()) {
                builders.get(s).addModifier(modifier);
            }
        } else {
            builders.get(section).addModifier(modifier);
        }

        return this;
    }

    @Override
    public ArmorSetBuilder category(ModItemCategory category) {
        for(ModItemBuilder builder : builders.values()) {
            builder.category(category);
        }

        return this;
    }

    @Override
    public ArmorSet build() {
        ArmorItemBuilder b1 = builders.get(GearManager.ArmorSection.HELMET);
        ArmorItemBuilder b2 = builders.get(GearManager.ArmorSection.CHESTPLATE);
        ArmorItemBuilder b3 = builders.get(GearManager.ArmorSection.LEGGINGS);
        ArmorItemBuilder b4 = builders.get(GearManager.ArmorSection.BOOTS);

        ModArmor head  = b1 == null ? null : b1.build();
        ModArmor chest = b2 == null ? null : b2.build();
        ModArmor legs  = b3 == null ? null : b3.build();
        ModArmor feet  = b4 == null ? null : b4.build();

        ArmorSet set = new ArmorSetImpl(head, chest, legs, feet);

        if(material != null) {
            if (head != null) {
                ShapedRecipe r = new ShapedRecipe(head.getId(), head.create());

                r.shape("AAA", "A A");

                material.setIngredient('A', r);

                Bukkit.addRecipe(r);
            }

            if (chest != null) {
                ShapedRecipe r = new ShapedRecipe(chest.getId(), chest.create());

                r.shape("A A", "AAA", "AAA");

                material.setIngredient('A', r);

                Bukkit.addRecipe(r);
            }

            if (legs != null) {
                ShapedRecipe r = new ShapedRecipe(legs.getId(), legs.create());

                r.shape("AAA", "A A", "A A");

                material.setIngredient('A', r);

                Bukkit.addRecipe(r);
            }

            if (feet != null) {
                ShapedRecipe r = new ShapedRecipe(feet.getId(), feet.create());

                r.shape("A A", "A A");

                material.setIngredient('A', r);

                Bukkit.addRecipe(r);
            }
        }

        return set;
    }
}
