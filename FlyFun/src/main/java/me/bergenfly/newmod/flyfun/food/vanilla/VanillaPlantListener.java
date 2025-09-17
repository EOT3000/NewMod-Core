package me.bergenfly.newmod.flyfun.food.vanilla;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.food.PlantsTypes;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Random;

public class VanillaPlantListener implements Listener {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager item = api.itemManager();

    Random random = new Random();

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        TreeType eventTreeSpecies = event.getSpecies();

        VanillaPlantType vanillaPlantType = VanillaPlantType.getVanillaPlantTypeFromTreeType(eventTreeSpecies);

        Fate shouldThePlantLive = chooseItsFate(event.getLocation().getBlock(), vanillaPlantType);

        switch (shouldThePlantLive) {
            case DIE: {
                VanillaPlantListener.block.getBlock(event.getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);

                event.getLocation().getBlock().setType(Material.DEAD_BUSH);

                System.out.println("dead >:D");
            }
            case REROLL: {
                event.setCancelled(true);
            }

            //If the plant lives, do nothing
        }
    }

    @EventHandler
    public void onPlantGrow(BlockGrowEvent event) {
        ModBlock modBlock = blockManager.getType(event.getBlock().getLocation());

        if(modBlock == PlantsTypes.DEAD_CACTUS_BLOCK) {
            return;
        }

        Block bukkitBlock = event.getBlock();
        VanillaPlantType vanillaPlantType = VanillaPlantType.getVanillaPlantTypeFromMaterial(bukkitBlock.getType());

        Fate shouldThePlantLive = chooseItsFate(event.getBlock(), vanillaPlantType);

        switch (shouldThePlantLive) {
            case DIE: {
                VanillaPlantListener.block.getBlock(event.getBlock().getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);

                switch (event.getBlock().getType()) {
                    //case
                }
            }
            case REROLL: {
                event.setCancelled(true);
            }

            //If the plant lives, do nothing
        }
    }

    private Fate chooseItsFate(Block block, VanillaPlantType vanillaPlantType) {
        if (vanillaPlantType == null) return Fate.LIVE;

        double temperature = block.getTemperature();
        double humidity = block.getHumidity();

        //TODO make this not hacky
        if (block.getBiome().translationKey().contains("savan")) {
            temperature -= .8;
            humidity += .2;
        }

        if (block.getBiome().translationKey().contains(".swamp")) {
            temperature -= .2;
        }

        double probabilityFinalFailure = vanillaPlantType.getData().probabilityFinalFailure((float) temperature, (float) humidity);
        double probabilityStageFailure = StagesProbabilityMath.stageLikelihoodFailure(probabilityFinalFailure, vanillaPlantType.getStages());
        double probabilityStageReroll = StagesProbabilityMath.probabilityReroll(probabilityStageFailure);

        double randomDouble = random.nextDouble();

        if (randomDouble < probabilityStageFailure) {
            return Fate.DIE;
        } else {
            randomDouble -= probabilityStageFailure;

            if (randomDouble < probabilityStageReroll) {
                return Fate.REROLL;
            } else {
                return Fate.LIVE;
            }
        }
    }

    /*@EventHandler
    public void onPlantGrow(BlockSpreadEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getNewState().getBlockData());
        System.out.println(event.getBlock());
        System.out.println();
        System.out.println();
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {
        System.out.println(event.toString());
        System.out.println(event.getBlocks());
        System.out.println(event.getBlock());
        System.out.println(event.getPlayer());
        System.out.println();
        System.out.println();
    }*/

    private enum Fate {
        LIVE,
        REROLL,
        DIE
    }
}
