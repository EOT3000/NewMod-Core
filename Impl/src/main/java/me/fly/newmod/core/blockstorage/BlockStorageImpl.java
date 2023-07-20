package me.fly.newmod.core.blockstorage;

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
    public boolean hasData(Location location, NamespacedKey key, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        return !(storage == null || storage.getByKey(IntTriple.fromLocation(location), key, type) == null);
    }

    @Override
    public Set<NamespacedKey> getKeys(Location location, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return new HashSet<>();
        }

        return storage.getKeys(IntTriple.fromLocation(location), type);
    }

    @Override
    public String getData(Location location, NamespacedKey key, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return null;
        }

        return storage.getByKey(IntTriple.fromLocation(location), key, type);
    }

    @Override
    public void setData(Location location, NamespacedKey key, String value, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return;
        }

        storage.modifyKey(IntTriple.fromLocation(location), key, value, type);
    }

    @Override
    public void removeData(Location location, NamespacedKey key, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return;
        }

        storage.removeKey(IntTriple.fromLocation(location), key, type);
    }

    @Override
    public void removeAllData(Location location, StorageType type) {
        RegionBlockStorage storage = getRegion(location);

        if(storage == null) {
            return;
        }

        storage.remove(IntTriple.fromLocation(location), type);
    }

    @Override
    public Set<Location> getAllStoredLocations(World world) {
        if(world == null) {
            return new HashSet<>();
        }

        worlds.putIfAbsent(world, new WorldBlockStorage(world));

        return worlds.get(world).getAllLocations();
    }
}
