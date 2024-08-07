package me.fly.newmod.flyfun.technology.util;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.util.IntUtil;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

//TODO: make all util classes final and prevent instantiation
public final class EnergyUtil {
    public static final NamespacedKey CHARGE = new NamespacedKey(FlyFunPlugin.get(), "charge");
    private static final NewModAPI api = FlyFunPlugin.get().api;


    public static int getCharge(Location block) {
        String data = api.blockStorage().getBlock(block).getData(CHARGE, BlockStorage.StorageType.BLOCK_DATA);
        return IntUtil.isInteger(data)
                ? Integer.parseInt(data)
                : 0;
    }

    public static void setCharge(Location block, int charge, int capacity) {
        api.blockStorage().getBlock(block).setData(CHARGE, Integer.toString(limitCapacity(charge, capacity)), BlockStorage.StorageType.BLOCK_DATA);
    }

    public static int addCharge(Location block, int charge, int capacity) {
        int newCharge = getCharge(block) + charge;

        api.blockStorage().getBlock(block).setData(CHARGE, Integer.toString(limitCapacity(newCharge, capacity)), BlockStorage.StorageType.BLOCK_DATA);

        return newCharge;
    }

    public static int subtractCharge(Location block, int charge, int capacity) {
        int newCharge = getCharge(block) - charge;

        api.blockStorage().getBlock(block).setData(CHARGE, Integer.toString(limitCapacity(newCharge, capacity)), BlockStorage.StorageType.BLOCK_DATA);

        return newCharge;
    }

    public static int limitCapacity(int charge, int capacity) {
        return Math.max(Math.min(charge, capacity), 0);
    }
}
