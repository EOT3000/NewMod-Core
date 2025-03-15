package me.bergenfly.newmod.core.armor.builder;

import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.armor.ArmorItemImpl;
import me.bergenfly.newmod.core.item.builder.ModItemBuilderImpl;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

public class ArmorItemBuilder extends ModItemBuilderImpl {
    private GearManager.ArmorSection section;
    private int toughness;
    private int armor;
    private int durability;

    @Override
    protected ModItem factory(NamespacedKey id, Material material, TextComponent component, ModBlock block, Class<? extends ModItemData> data, List<MetaModifier> modifiers) {
        return new ArmorItemImpl(id, material, component, block, data, modifiers, section, armor, toughness, durability);
    }

    public ArmorItemBuilder(Material material, NamespacedKey key, GearManager.ArmorSection section, int armor, int toughness, int durability) {
        super(material, key);

        this.section = section;
        this.toughness = toughness;
        this.armor = armor;
        this.durability = durability;
    }

    @Override
    public ArmorItemImpl build() {
        return (ArmorItemImpl) super.build();
    }
}
