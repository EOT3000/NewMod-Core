package me.bergenfly.newmod.core.armor;

import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.item.ModArmor;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.ArmorModifier;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.item.builder.BuiltModItemImpl;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

public class ArmorItemImpl extends BuiltModItemImpl implements ModArmor {
    private final int toughness;
    private final int armor;
    private final int durability;

    public ArmorItemImpl(NamespacedKey id, Material material, TextComponent component, ModBlock block, Class<? extends ModItemData> data, List<MetaModifier> modifiers,
                         int armor, int toughness, int durability) {
        super(id, material, component, block, data, modifiers);

        this.toughness = toughness;
        this.armor = armor;
        this.durability = durability;

        modifiers.add(new ArmorModifier(id.getNamespace(), armor, toughness, durability));
    }

    @Override
    public int getToughness() {
        return toughness;
    }

    @Override
    public int getArmor() {
        return armor;
    }
}
