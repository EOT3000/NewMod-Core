package me.bergenfly.newmod.core.api.item;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class VanillaItem implements Item {
    private static final Map<Material, VanillaItem> vanillaItems = new HashMap<>();

    private final Material material;

    private VanillaItem(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public static VanillaItem fromMaterial(Material material) {
        if(material == null || !material.isItem()) {
            return null;
        }

        vanillaItems.putIfAbsent(material, new VanillaItem(material));

        return vanillaItems.get(material);
    }
}
