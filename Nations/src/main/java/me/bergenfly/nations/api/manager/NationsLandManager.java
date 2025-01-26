package me.bergenfly.nations.api.manager;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.model.plot._1x1_Chunk;
import me.bergenfly.nations.impl.registry.RegistryImpl;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class NationsLandManager {
    private Registry<ClaimedChunk, Integer> PLOTS = new RegistryImpl<>(ClaimedChunk.class);

    public NationsLandManager() {
        Plots.init();
    }

    public void replaceChunk(ClaimedChunk old, ClaimedChunk neew) {
        old.unclaim();
        PLOTS.set(Plots.getLocationId(neew.getChunkX(), neew.getChunkZ(), neew.getWorld()), neew);
    }

    public ClaimedChunk getClaimedChunkAtLocation(Location location) {
        return getClaimedChunkAtChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ());
    }

    public ClaimedChunk getClaimedChunkAtLocation(World w, int x, int z) {
        return getClaimedChunkAtChunk(w, x/16, z/16);
    }

    public ClaimedChunk getClaimedChunkAtChunk(Chunk chunk) {
        return getClaimedChunkAtChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public ClaimedChunk getClaimedChunkAtChunk(World w, int chunkX, int chunkZ) {
        return PLOTS.get(Plots.getLocationId(chunkX, chunkZ, w));
    }

    public void unclaimChunk(World w, int chunkX, int chunkZ) {
        int id = Plots.getLocationId(chunkX, chunkZ, w);
        PLOTS.get(id).unclaim();
        PLOTS.set(id, null);
    }

    public boolean tryClaimChunkAtLocation(Location location, LandAdministrator administrator) {
        return tryClaimChunk(location.getChunk(), administrator);
    }

    public boolean tryClaimChunk(Chunk chunk, LandAdministrator administrator) {
        return tryClaimChunk(chunk.getWorld(), chunk.getX(), chunk.getZ(), administrator);
    }

    public boolean tryClaimChunk(World w, int chunkX, int chunkZ, LandAdministrator administrator) {
        int id = Plots.getLocationId(chunkX, chunkZ, w);
        if(PLOTS.get(id) != null) {
            return false;
        }

        _1x1_Chunk c = new _1x1_Chunk(chunkX, chunkZ, w, administrator);

        administrator.addLand(c.getAt(0,0));

        PLOTS.set(id, c);

        return true;
    }

    public PlotSection getPlotSectionAtLocation(Location location) {
        ClaimedChunk chunk = getClaimedChunkAtChunk(location.getChunk());
        return chunk == null ? null : chunk.getAt(location.getBlockX()-chunk.getChunkX()*16, location.getBlockZ()-chunk.getChunkZ()*16);
    }

    public Registry<ClaimedChunk, Integer> getPLOTS() {
        return PLOTS;
    }
}
