package me.bergenfly.newmod.flyfun.fortunefix;

import me.bergenfly.newmod.core.util.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FortuneDistributions {
    //TODO: make this not a static class, make it a singleton or something
    static {
        addDrop(Material.DIAMOND_ORE, Material.DIAMOND, FortuneDistribution.oneOrTwo);
        addDrop(Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND, FortuneDistribution.oneOrTwo);
        addDrop(Material.EMERALD_ORE, Material.EMERALD, FortuneDistribution.oneOrTwo);
        addDrop(Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD, FortuneDistribution.oneOrTwo);

        addDrop(Material.GOLD_ORE, Material.RAW_GOLD, FortuneDistribution.oneOrTwo);
        addDrop(Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD, FortuneDistribution.oneOrTwo);

        addDrop(Material.COAL_ORE, Material.COAL, FortuneDistribution.oneToThree);
        addDrop(Material.DEEPSLATE_COAL_ORE, Material.COAL, FortuneDistribution.oneToThree);
        addDrop(Material.IRON_ORE, Material.RAW_IRON, FortuneDistribution.oneToThree);
        addDrop(Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON, FortuneDistribution.oneToThree);
        addDrop(Material.COPPER_ORE, Material.RAW_COPPER, FortuneDistribution.oneToThree);
        addDrop(Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER, FortuneDistribution.oneToThree);

        addDrop(Material.NETHER_QUARTZ_ORE, Material.QUARTZ, FortuneDistribution.oneToThreeAddOneThirdFortune);

        addDrop(Material.LAPIS_ORE, Material.LAPIS_LAZULI, FortuneDistribution.twoTo_ThreePlusFortune_AddOneHalfFortune);
        addDrop(Material.DEEPSLATE_LAPIS_ORE, Material.LAPIS_LAZULI, FortuneDistribution.twoTo_ThreePlusFortune_AddOneHalfFortune);

        addDrop(Material.REDSTONE_ORE, Material.REDSTONE, FortuneDistribution.twoTo_ThreePlusFortune_AddOneHalfFortune);
        addDrop(Material.DEEPSLATE_REDSTONE_ORE, Material.REDSTONE, FortuneDistribution.twoTo_ThreePlusFortune_AddOneHalfFortune);

        addDrop(Material.NETHER_GOLD_ORE, Material.GOLD_NUGGET, FortuneDistribution.fourToSixteenAddOneTenthFortune);
    }

    private static final Map<Material, Pair<Material, FortuneDistribution>> distributions = new HashMap<>();

    public static ItemStack createDrop(Material block, int fortune) {
        Pair<Material, FortuneDistribution> pair = distributions.get(block);

        return new ItemStack(pair.getKey(), pair.getValue().generateRandomItemCount(fortune));
    }

    public static void addDrop(Material block, Material item, FortuneDistribution distribution) {
        distributions.put(block, new Pair<>(item, distribution));
    }

    public static Material getDropItem(Material block) {
        return distributions.get(block).getKey();
    }
}
