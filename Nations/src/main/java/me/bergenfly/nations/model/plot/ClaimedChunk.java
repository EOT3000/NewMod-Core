package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.manager.Plots;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.serializer.Serializable;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClaimedChunk implements Serializable {
    private DivisionStorage storage;

    private final int chunkX;
    private final int chunkZ;
    private final World world;

    public ClaimedChunk(int chunkX, int chunkZ, World world) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
        this.storage = new DivisionStorage(0);
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public World getWorld() {
        return world;
    }

    public DivisionStorage getStorage() {
        return storage;
    }

    @Override
    public Object serialize() {
        Map<String, Object> ret = new HashMap<>();

        Map<String, Object> data = new HashMap<>();

        data.put("chunkX", chunkX);
        data.put("chunkZ", chunkZ);
        data.put("world", Plots.getWorldId(world));
        data.put("storage", storage);

        return null;
    }

    @Override
    public String getId() {
        return "" + Plots.getLocationId(chunkX, chunkZ, world);
    }
}
