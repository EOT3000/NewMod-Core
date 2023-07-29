package me.fly.newmod.flyfun.plants;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.category.ModItemCategory;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.plants.block.Seedling;
import me.fly.newmod.flyfun.plants.block.TreeTap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

import java.util.function.Consumer;

public class PlantsTypes {
    public static void init() {

    }

    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final ItemManager item = api.itemManager();

    public static final ModItemCategory PLANTS = api.categoryManager().createCategory(new NamespacedKey(FlyFunPlugin.get(), "plants_category"),
            Material.FLOWERING_AZALEA, Component.text("Plants").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false), Component.text(""), Component.text("Plants and farming").decoration(TextDecoration.ITALIC, false));

    public static final ModItem TREE_TAP = item.createBuilder(Material.TRIPWIRE_HOOK, plugin, "tree_tap").block(new TreeTap()).displayName("Tree Tap", 0xbfb69d).category(PLANTS).build();

    public static final ModItem RED_CHERRIES = item.createBuilder(Material.SWEET_BERRIES, plugin, "red_cherries").block(new Seedling("red_cherry_seeds", new VanillaSeedlingConsumer(Material.CHERRY_SAPLING))).displayName("Red Cherries", NamedTextColor.DARK_RED).category(PLANTS).build();
    public static final ModItem ACACIA_SEEDS = item.createBuilder(Material.MELON_SEEDS, plugin, "acacia_seeds").displayName("Acacia Seeds", NamedTextColor.DARK_GRAY).category(PLANTS).build();


    private static class VanillaSeedlingConsumer implements Consumer<Block> {
        private final Material set;

        public VanillaSeedlingConsumer(Material set) {
            this.set = set;
        }

        @Override
        public void accept(Block block) {
            System.out.println("accepted block: " + block.getType());
            System.out.println("accepted block: " + block.getLocation());
            System.out.println();

            PlantsTypes.block.getBlock(block.getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);

            System.out.println("set to " + set);

            block.setType(set);

            System.out.println();
        }
    }
}
