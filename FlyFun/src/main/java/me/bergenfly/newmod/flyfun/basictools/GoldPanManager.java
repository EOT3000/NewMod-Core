package me.bergenfly.newmod.flyfun.basictools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GoldPanManager {
    private final Map<Material, Map<ItemStack, Integer>> goldPanChances = new HashMap<>();
    private final Map<Material, Integer> totalValues = new HashMap<>();

    public void setPanChance(Material block, ItemStack item, int chance) {
        goldPanChances.putIfAbsent(block, new HashMap<>());

        goldPanChances.get(block).put(item, chance);

        int tc = 0;

        for(int value : goldPanChances.get(block).values()) {
            tc+=value;
        }

        totalValues.put(block, tc);
    }

    public int getPanChance(Material block, ItemStack item) {
        if(block == null || item == null || goldPanChances.get(block) == null || goldPanChances.get(block).get(item) == null) {
            return 0;
        }

        return goldPanChances.get(block).get(item);
    }

    public Map<ItemStack, Integer> getPossibleItems(Material block) {
        return block == null || goldPanChances.get(block) == null ? new HashMap<>() : new HashMap<>(goldPanChances.get(block));
    }

    public int getTotalValue(Material block) {
        return totalValues.getOrDefault(block, 0);
    }
}
