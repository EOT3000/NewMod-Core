package me.bergenfly.newmod.flyfun.food.vanilla;

import org.bukkit.Material;
import org.bukkit.block.Block;

import static org.bukkit.Material.*;

public class DeadPlants {



    public void killPropagule(Block block) {

    }

    public void killSapling(Block block) {
        //block.setType();
    }

    public void killSweetBerryBush(Block block, int age) {

    }

    //Pumpkins, melons
    public void killGourdStalk(Block block, int age) {

    }

    public void killBeetroots(Block block, int age) {

    }

    public void killWheat(Block block, int age) {

    }

    public void killCarrotsPotatoes(Block block, int age) {

    }

    public enum DeadReplacements {
        DEAD_SEEDS(DEAD_BUSH, CARROT, 1, -1),
        DEAD_ROOTS(DEAD_BUSH, CARROT, 5, -1),
        DEAD_WHEAT_STALKS(DEAD_BUSH, CARROT, 3, -1),
        DEAD_WHEAT(DEAD_BUSH, CARROT, 6, -1),
        DEAD_PROPAGULE(DEAD_BUSH, MANGROVE_PROPAGULE, 1, 1),
        DEAD_CACTUS(CACTUS, CACTUS, 1, -1);

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
