package me.bergenfly.newmod.flyfun.technology.util;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.util.IntUtil;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.technology.EnergyComponent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

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

    public static Block getDestination(Block current, int modX, int modY, int modZ, int stack) {
        if(stack > 255) {
            return null;
        }

        Block next = current.getRelative(modX, modY, modZ);

        ModBlock block = api.blockManager().getType(next.getLocation());

        if(block instanceof EnergyComponent e) {
            return e.getDestination(current, next, stack+1);
        }

        return null;
    }

    public static int push(Block current, int modX, int modY, int modZ, int energy) {
        Block d = getDestination(current, modX, modY, modZ, 0);

        if(d == null) {
            return 0;
        }

        return ((EnergyComponent) api.blockManager().getType(d)).push(current, d, energy);
    }
}
