package me.fly.newmod.core.blockstorage;

import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.blockstorage.StoredBlock;
import me.fly.newmod.core.api.util.IntTriple;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockStorageImpl implements BlockStorage {
    @Override
    public StoredBlock getBlock(Location location) {
        return null;
    }

    @Override
    public Set<Location> getAllStoredLocations(Chunk chunk) {
        return null;
    }
}
