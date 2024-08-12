package me.bergenfly.newmod.core.blockstorage;

import com.jeff_media.customblockdata.CustomBlockData;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.blockstorage.StoredBlock;
import me.bergenfly.newmod.core.NewModPlugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockStorageImpl implements BlockStorage {
    @Override
    public StoredBlock getBlock(Location location) {
        return new StoredBlockImpl(location);
    }

    @Override
    public Set<Location> getAllStoredLocations(Chunk chunk) {
        Set<Location> locations = new HashSet<>();

        for(Block block : CustomBlockData.getBlocksWithCustomData(NewModPlugin.get(), chunk)) {
            locations.add(block.getLocation());
        }

        return locations;
    }
}
