package me.fly.newmod.core.impl.blockstorage;

import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.util.IntTriple;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockStorageImpl implements BlockStorage {
    private final Map<World, WorldBlockStorage> worlds = new HashMap<>();

    private RegionBlockStorage getRegion(Location location) {
        WorldBlockStorage storage = worlds.get(location.getWorld());

        int x = location.getChunk().getX() >> 5;
        int z = location.getChunk().getZ() >> 5;

        return storage.getRegion(x, z);
    }

    @Override
    public boolean hasData(Location location, NamespacedKey key) {
        RegionBlockStorage storage = getRegion(location);

        return !(storage == null || storage.getByKey(IntTriple.fromLocation(location), key) == null);
    }

    @Override
    public Set<NamespacedKey> getKeys(Location location) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return new HashSet<>();
        }

        return storage.getKeys(IntTriple.fromLocation(location));
    }

    @Override
    public String getData(Location location, NamespacedKey key) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return null;
        }

        return storage.getByKey(IntTriple.fromLocation(location), key);
    }

    @Override
    public void setData(Location location, NamespacedKey key, String value) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return;
        }

        storage.modifyKey(IntTriple.fromLocation(location), key, value);
    }

    @Override
    public void removeData(Location location, NamespacedKey key) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return;
        }

        storage.removeKey(IntTriple.fromLocation(location), key);
    }

    @Override
    public Set<Location> getAllStoredLocations(World world) {
        return worlds.get(world).getAllLocations();
    }
}
