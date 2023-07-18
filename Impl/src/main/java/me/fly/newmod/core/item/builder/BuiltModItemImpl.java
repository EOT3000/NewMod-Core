package me.fly.newmod.core.item.builder;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.builder.meta.MetaModifier;
import me.fly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BuiltModItemImpl implements ModItem {
    private final NamespacedKey id;
    private final Material material;
    private final TextComponent component;

    private final ModBlock block;
    private final Class<? extends ModItemData> data;

    private final List<MetaModifier> modifiers;


    public BuiltModItemImpl(NamespacedKey id, Material material, TextComponent component,
                            ModBlock block, Class<? extends ModItemData> data, List<MetaModifier> modifiers) {
        this.id = id;
        this.material = material;
        this.component = component;
        this.block = block;
        this.data = data;
        this.modifiers = modifiers;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Component getDisplayName() {
        return component;
    }

    @Override
    public Class<? extends ModItemData> getDataType() {
        return data;
    }

    @Override
    public ModBlock getBlock() {
        return block;
    }

    @Override
    public ItemStack applyModifiers(ItemStack stack) {
        for(MetaModifier modifier : modifiers) {
            modifier.apply(stack);
        }

        return stack;
    }
}
