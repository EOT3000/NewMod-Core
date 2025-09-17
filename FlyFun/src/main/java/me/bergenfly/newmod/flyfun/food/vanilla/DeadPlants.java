package me.bergenfly.newmod.flyfun.food.vanilla;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

import static org.bukkit.Material.*;

public class DeadPlants {

    public static final NamespacedKey DEAD_DISPLAY_TYPE = new NamespacedKey("newmod-core", "dead_display_type");
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage blockStorage = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager itemManager = api.itemManager();

    public static void killPropagule(Block block) {
        setBlockTo(block, DeadReplacements.DEAD_PROPAGULE);
    }

    public static void killSapling(Block block) {
        block.setType(DEAD_BUSH);
    }

    public static void killSweetBerryBush(Block block, int age) {
        if(in(age, 1,3)) {
            block.setType(DEAD_BUSH);

            return;
        }

        setBlockTo(block, DeadReplacements.DEAD_SMALL_BUSH);
    }

    //Pumpkins, melons
    public static void killGourdStalk(Block block, int age) {

    }

    public static void killBeetroots(Block block, int age) {

    }

    public static void killWheat(Block block, int age) {

    }

    public static void killCarrotsPotatoes(Block block, int age) {

    }

    public static void killCactus(Block block) {

    }

    public static void killSugarCane(Block block) {

    }

    private static void setBlockTo(Block block, DeadReplacements replacements) {
        blockStorage.getBlock(block.getLocation()).setData(DEAD_DISPLAY_TYPE, replacements.name(), BlockStorage.StorageType.BLOCK_DATA);

    }

    //Is compare within min,max, inclusive?
    private static boolean in(int compare, int min, int max) {
        return compare >= min && compare <= max;
    }

    public enum DeadReplacements {
        DEAD_SEEDS(DEAD_BUSH, CARROT, 1, -1),
        DEAD_ROOTS(DEAD_BUSH, CARROT, 5, -1),
        DEAD_WHEAT_STALKS(DEAD_BUSH, CARROT, 3, -1),
        DEAD_WHEAT(DEAD_BUSH, CARROT, 6, -1),
        DEAD_PROPAGULE(DEAD_BUSH, MANGROVE_PROPAGULE, 1, 1),
        DEAD_CACTUS(CACTUS, CACTUS, 1, -1),
        DEAD_SMALL_BUSH(DEAD_BUSH, CACTUS, 1, -1);

        private final Material serverSideType;
        private final Material clientSideType;
        private final int clientSideAge;
        private final int clientSideStage;

        DeadReplacements(Material serverSideType, Material clientSideType, int clientSideAge, int clientSideStage) {
            this.serverSideType = serverSideType;
            this.clientSideType = clientSideType;
            this.clientSideAge = clientSideAge;
            this.clientSideStage = clientSideStage;
        }


        public Material getServerSideType() {
            return serverSideType;
        }

        public Material getClientSideType() {
            return clientSideType;
        }

        public int getClientSideAge() {
            return clientSideAge;
        }

        public int getClientSideStage() {
            return clientSideStage;
        }
    }
}
