package me.bergenfly.newmod.flyfun.food.vanilla;

import org.bukkit.TreeType;

import static org.bukkit.TreeType.*;

public enum VanillaPlant {

    //OAK_TREE(.95f,  TREE, BIG_TREE)

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
