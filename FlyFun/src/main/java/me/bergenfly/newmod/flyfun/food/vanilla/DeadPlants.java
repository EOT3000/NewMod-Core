package me.bergenfly.newmod.flyfun.food.vanilla;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static org.bukkit.Material.*;

public class DeadPlants {

    public static final NamespacedKey DEAD_DISPLAY_MATERIAL = new NamespacedKey("newmod-core", "dead_display_material");
    public static final NamespacedKey DEAD_DISPLAY_DATA = new NamespacedKey("newmod-core", "dead_display_age");
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage blockStorage = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager itemManager = api.itemManager();

    public static void killPropagule(Block block) {
        setBlockTo(block, DeadReplacement.DEAD_PROPAGULE);
    }

    public static void killSapling(Block block) {
        block.setType(DEAD_BUSH);
    }

    public static void killSweetBerryBush(Block block, int age) {
        if(in(age, 1,3)) {
            block.setType(DEAD_BUSH);

            return;
        }

        block.setType(SHORT_DRY_GRASS); //Does this align weird? TODO check
    }

    //Pumpkins, melons
    public static void killGourdStalk(Block block, int age) {

    }

    public static void killBeetroots(Block block, int age) {
        if (in(age, 0, 1)) {
            setBlockTo(block, DeadReplacement.DEAD_SEEDS);

            return;
        }

        setBlockTo(block, DeadReplacement.DEAD_ROOTS);
    }

    public static void killWheat(Block block, int age) {
        if (in(age, 0, 2)) {
            setBlockTo(block, DeadReplacement.DEAD_SEEDS);

            return;
        }

        if (in(age, 3, 5)) {
            setBlockTo(block, DeadReplacement.DEAD_WHEAT_STALKS);

            return;
        }

        setBlockTo(block, DeadReplacement.DEAD_WHEAT);
    }

    public static void killCarrotsPotatoes(Block block, int age) {
        if (in(age, 0, 3)) {
            setBlockTo(block, DeadReplacement.DEAD_SEEDS);

            return;
        }

        //if (in(age, 4, 7)) {
            setBlockTo(block, DeadReplacement.DEAD_ROOTS);
        //}
    }

    public static void killCactus(Block block) {
        setBlockTo(block, DeadReplacement.DEAD_CACTUS);
    }

    public static void killSugarCane(Block block) {
        setBlockTo(block, DeadReplacement.DEAD_SUGAR_CANE);
    }

    private static void setBlockTo(Block block, DeadReplacement replacement) {
        blockStorage.getBlock(block.getLocation()).setData(DEAD_DISPLAY_MATERIAL, replacement.clientSideType.name(), BlockStorage.StorageType.BLOCK_DATA);
        blockStorage.getBlock(block.getLocation()).setData(DEAD_DISPLAY_DATA, replacement.clientSideData, BlockStorage.StorageType.BLOCK_DATA);

        block.setType(replacement.serverSideType);


        for(Player player : block.getChunk().getPlayersSeeingChunk()) {
            api.chunkController().sendPhantomBlock(block.getLocation(), player, replacement.clientSideType, replacement.clientSideData);
        }
    }

    //Is compare within min,max, inclusive?
    private static boolean in(int compare, int min, int max) {
        return compare >= min && compare <= max;
    }

    public enum DeadReplacement {
        DEAD_SEEDS(DEAD_BUSH, CARROTS, "[age=1]"),
        DEAD_ROOTS(DEAD_BUSH, CARROTS, "[age=5]"),
        DEAD_WHEAT_STALKS(DEAD_BUSH, CARROTS, "[age=3]"),
        DEAD_WHEAT(DEAD_BUSH, CARROTS, "[age=6]"),
        DEAD_PROPAGULE(DEAD_BUSH, MANGROVE_PROPAGULE, "[age=1,stage=1,hanging=false,waterlogged=false]"),
        DEAD_CACTUS(CACTUS, CACTUS, "[age=2]"),
        DEAD_SUGAR_CANE(SUGAR_CANE, SUGAR_CANE, "[age=2]");

        //DEAD_SMALL_BUSH(DEAD_BUSH, SHORT_DRY_GRASS, -1, -1);

        private final Material serverSideType;
        private final Material clientSideType;
        private final String clientSideData;

        DeadReplacement(Material serverSideType, Material clientSideType, String clientSideData) {
            this.serverSideType = serverSideType;
            this.clientSideType = clientSideType;
            this.clientSideData = clientSideData;
        }


        public Material getServerSideType() {
            return serverSideType;
        }

        public Material getClientSideType() {
            return clientSideType;
        }

        public String getClientSideData() {
            return clientSideData;
        }
    }
}
