package me.fly.newmod.flyfun.magic.item;

import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class RepairedSpawner implements ModItem {
    @Override
    public NamespacedKey getId() {
        return null;
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public Class<? extends ModItemData> getDataType() {
        return null;
    }

    @Override
    public ModBlock getBlock() {
        return null;
    }

    @Override
    public ItemStack applyModifiers(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack create() {
        return null;
    }
}
