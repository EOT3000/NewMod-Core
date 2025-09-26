package me.bergenfly.newmod.flyfun.food.vanilla;

import org.bukkit.Material;
import org.bukkit.TreeType;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.TreeType.*;
import static org.bukkit.Material.*;

public enum VanillaPlantType {

    //TODO: savanna should have humidity .2, not 0, and temp 1.2, not 2
    //Swamp should have temp .6, not .8

    OAK_TREE(.75f, .85f, .45f, 0.02f,
            .6f, .3f, .25f, 0.02f, 1, TREE, BIG_TREE),
    SPRUCE_TREE(.1f, .7f, .25f, 0.025f,
            .55f, .3f, .25f, 0.025f, 1, REDWOOD, MEGA_REDWOOD, TALL_REDWOOD, MEGA_PINE),
    ACACIA_TREE(1.425f, .525f, .275f, 0.025f,
            .45f, .425f, .75f, 0.01f, 1, ACACIA),

    //TODO: redo these down
    BIRCH_TREE(.4f, .35f, .3f, 0.01f,
            .6f, .25f, .15f, 0.01f, 1, BIRCH, TALL_BIRCH),
    DARK_OAK_TREE(.7f, .25f, .15f, 0.01f,
            .8f, .15f, .1f, 0.01f, 1, DARK_OAK),
    JUNGLE_TREE(.95f, .1f, .35f, 0.03f,
            .85f, .25f, .15f, 0.03f, 1, JUNGLE, JUNGLE_BUSH, SMALL_JUNGLE),
    MANGROVE_TREE(.8f, .15f, .2f, 0.04f,
            .95f, .025f, .075f, 0.04f, 1, MANGROVE, TALL_MANGROVE),
    /*AZALEA_TREE(.65f, .35f, .2f, 0.01f,
            .6f, .45f, .3f, 0.01f, 1, TreeType.AZALEA),*/
    CHERRY_TREE(.5f, .25f, .775f, 0.045f,
            .6f, .275f, .175f, 0.045f, 1, CHERRY),
    PALE_OAK_TREE(.7f, .2f, .15f, 0.015f,
            .8f, .15f, .1f, 0.015f, 1, PALE_OAK),

    BEETROOT_CROP(.6f, .95f, .5f, 0.01f,
            .525f, .3f, .45f, 0.01f, 3, BEETROOT_SEEDS),
    WHEAT_CROP(.85f, .6f, .4f, 0.01f,
            .5f, .4f, .3f, 0.01f, 7, WHEAT),
    POTATO_CROP(.75f, .7f, .5f, 0.01f,
            .5f, .35f, .3f, 0.01f, 7, POTATOES),
    CARROT_CROP(.75f, .7f, .5f, 0.01f,
            .5f, .35f, .3f, 0.01f, 7, CARROTS),
    MELON_CROP(1.35f, .5f, .85f, 0.01f,
            .6f, .575f, .35f, 0.01f, 8, MELON_STEM),
    PUMPKIN_CROP(.3f, 1.05f, .8f, 0.01f,
            .55f, .45f, .5f, 0.01f, 8, PUMPKIN_STEM),

    SWEET_BERRY_BUSH(.05f, .7f, .5f, 0.001f,
            .55f, .35f, .5f, 0.00f, 3, Material.SWEET_BERRY_BUSH),

    /*BAMBOO(.8f, .6f, .45f, 0.005f,
            .7f, .7f, .45f, 0.005f, 12, Material.BAMBOO, BAMBOO_SAPLING),*/

    CACTUS(1.7f, .875f, .2f, 0.005f,
            .1f, .275f, .2f, 0.00f, 2, Material.CACTUS),

    SUGAR_CANE(.9f, .275f, .8f, 0.01f,
            .575f, .35f, .6f, 0.00f, 2, Material.SUGAR_CANE),


    ;

    private static final Map<TreeType, VanillaPlantType> treeType2VanillaPlantTypeMap = new HashMap<>();
    private static final Map<Material, VanillaPlantType> material2VanillaPlantTypeMap = new HashMap<>();

    static {
        for(VanillaPlantType type : values()) {
            for (Object key : type.keys) {
                if (key instanceof TreeType) {
                    treeType2VanillaPlantTypeMap.put((TreeType) key, type);
                }

                if (key instanceof Material) {
                    material2VanillaPlantTypeMap.put((Material) key, type);
                }
            }
        }
    }

    public static VanillaPlantType getVanillaPlantTypeFromTreeType(TreeType type) {
        return treeType2VanillaPlantTypeMap.get(type);
    }

    public static VanillaPlantType getVanillaPlantTypeFromMaterial(Material type) {
        return material2VanillaPlantTypeMap.get(type);
    }

    private final Object[] keys;
    private final TempHumidDistribution data;
    private final int stages;

    VanillaPlantType(TempHumidDistribution data, int stages, TreeType... keys) {
        this.keys = keys;
        this.data = data;
        this.stages = stages;
    }

    VanillaPlantType(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                     float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity, int stages, TreeType... keys) {
        this(TempHumidDistribution.create(idealTemp, ninetyPercentTempDif, tenPercentTempDif, additionalTempSensitivity,
                idealHumid, ninetyPercentHumidDif, tenPercentHumidDif, additionalHumidSensitivity), stages, keys);
    }

    VanillaPlantType(TempHumidDistribution data, int stages, Material... keys) {
        this.keys = keys;
        this.data = data;
        this.stages = stages;
    }

    VanillaPlantType(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                     float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity, int stages, Material... keys) {
        this(TempHumidDistribution.create(idealTemp, ninetyPercentTempDif, tenPercentTempDif, additionalTempSensitivity,
                idealHumid, ninetyPercentHumidDif, tenPercentHumidDif, additionalHumidSensitivity), stages, keys);
    }

    public int getStages() {
        return stages;
    }

    public Object[] getKeys() {
        return keys;
    }

    public TempHumidDistribution getData() {
        return data;
    }
}
