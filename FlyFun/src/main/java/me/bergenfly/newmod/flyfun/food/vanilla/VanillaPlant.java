package me.bergenfly.newmod.flyfun.food.vanilla;

import org.bukkit.TreeType;

import static org.bukkit.TreeType.*;
import static org.bukkit.Material.*;

public enum VanillaPlant {

    //TODO: savanna should have humidity .2, not 0, and temp 1.2, not 2
    //Swamp should have temp .6, not .8

    OAK_TREE(.6f, 1.0f, .4f, 0.02f,
            .6f, .65f, .45f, 0.02f,  TREE, BIG_TREE),
    SPRUCE_TREE(.15f, 1.0f, .65f, 0.025f,
            .6f, .4f, .35f, 0.025f, REDWOOD, MEGA_REDWOOD, TALL_REDWOOD, MEGA_PINE),
    ACACIA_TREE(1.2f, .85f, .3f, 0.025f,
                   .2f, .5f, .3f, 0.01f, ACACIA),
    BIRCH_TREE(.55f, .4f, .3f, 0.01f,
            .7f, .4f, .25f, 0.01f, BIRCH, TALL_BIRCH),
    DARK_OAK_TREE(.7f, .25f, .15f, 0.01f,
            .8f, .15f, .1f, 0.01f, DARK_OAK),
    JUNGLE_TREE(.95f, .2f, .15f, 0.03f,
            .85f, .25f, .15f, 0.03f, JUNGLE, JUNGLE_BUSH, SMALL_JUNGLE),
    MANGROVE_TREE(.8f, .22f, .12f, 0.04f,
            .9f, .1f, .07f, 0.04f, MANGROVE, TALL_MANGROVE),
    AZALEA_TREE(.65f, .35f, .2f, 0.01f,
            .6f, .45f, .3f, 0.01f, TreeType.AZALEA),
    CHERRY_TREE(.5f, .4f, .3f, 0.045f,
            .8f, .55f, .3f, 0.045f, CHERRY),
    PALE_OAK_TREE(.7f, .2f, .15f, 0.015f,
            .8f, .15f, .1f, 0.015f, PALE_OAK),

    BEETROOT_CROP(.7f, .85f, .5f, 0.01f,
            .5f, .4f, .3f, 0.01f),
    WHEAT_CROP(.85f, .6f, .4f, 0.01f,
            .5f, .4f, .3f, 0.01f),
    POTATO_CROP(.75f, .7f, .5f, 0.01f,
            .5f, .35f, .3f, 0.01f),
    CARROT_CROP(.75f, .7f, .5f, 0.01f,
            .5f, .35f, .3f, 0.01f),
    MELON_CROP(1.35f, 1.15f, .85f, 0.01f,
            .55f, .75f, .5f, 0.01f),
    PUMPKIN_CROP(.3f, 1.05f, .8f, 0.01f,
            .55f, .75f, .5f, 0.01f),

    SWEET_BERRY_BUSH(.3f, 1.05f, .8f, 0.01f,
            .55f, .75f, .5f, 0.01f),

    BAMBOO(.8f, .6f, .45f, 0.005f,
            .7f, .7f, .45f, 0.005f),

    ;

    private final Object[] key;
    private final TempHumidDistribution data;


    VanillaPlant(TempHumidDistribution data, TreeType... keys) {
        this.key = keys;
        this.data = data;
    }

    VanillaPlant(float idealTemp, float ninetyPercentTempDif, float tenPercentTempDif, float additionalTempSensitivity,
                 float idealHumid, float ninetyPercentHumidDif, float tenPercentHumidDif, float additionalHumidSensitivity, TreeType... keys) {
        this(TempHumidDistribution.create(idealTemp, ninetyPercentTempDif, tenPercentTempDif, additionalTempSensitivity,
                idealHumid, ninetyPercentHumidDif, tenPercentHumidDif, additionalHumidSensitivity), keys);
    }

    public Object[] getKey() {
        return key;
    }

    public TempHumidDistribution getData() {
        return data;
    }
}
