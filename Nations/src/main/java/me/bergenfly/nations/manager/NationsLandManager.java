package me.bergenfly.nations.manager;


import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.Lot;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.registry.RegistryImpl;
import me.bergenfly.nations.util.ChunkLocation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NationsLandManager {
    private Registry<ClaimedChunk, Integer> PLOTS = new RegistryImpl<>(ClaimedChunk.class);

    private Map<LandAdministrator, IntSet> administratorClaims = new HashMap<>();

    public NationsLandManager() {
        Plots.init();
    }

    public ClaimedChunk getClaimedChunkAtLocation(Location location) {
        return getClaimedChunkAtChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ());
    }

    public ClaimedChunk getClaimedChunkAtChunk(Chunk chunk) {
        return getClaimedChunkAtChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public ClaimedChunk getClaimedChunkAtChunk(World w, int chunkX, int chunkZ) {
        simplifyAt(PLOTS.get(Plots.getLocationId(chunkX, chunkZ, w)));
        return PLOTS.get(Plots.getLocationId(chunkX, chunkZ, w));
    }

    public ClaimedChunk tryClaimFullChunkOtherwiseFail(Chunk chunk, @Nullable LandAdministrator administrator) {
        return tryClaimFullChunkOtherwiseFail(chunk.getWorld(), chunk.getX(), chunk.getZ(), administrator);
    }

    public ClaimedChunk tryClaimFullChunkOtherwiseFail(World w, int chunkX, int chunkZ, @Nullable LandAdministrator administrator) {
        int id = Plots.getLocationId(chunkX, chunkZ, w);

        ClaimedChunk chunk = PLOTS.get(id);

        if(chunk != null) {
            simplifyAt(chunk);

            if (PLOTS.get(id) != null) {
                return null;
            }
        }

        ClaimedChunk newChunk = new ClaimedChunk(chunkX, chunkZ, w);

        newChunk.getStorage().setAt(0, 0, administrator, administrator);

        administratorClaims.putIfAbsent(administrator, new IntOpenHashSet());

        administratorClaims.get(administrator).add(Plots.getLocationId(newChunk));

        PLOTS.set(id, newChunk);

        return newChunk;
    }

    //public boolean tryUnclaimFullChunkOtherwiseFail()

    private void simplifyAt(ClaimedChunk chunk) {
        if(chunk == null) {
            return;
        }

        //if(chunk.getSections(false).length == 0) {
        //    PLOTS.set(Plots.getLocationId(chunk), null);
        //}
    }

    public Registry<ClaimedChunk, Integer> chunksRegistry() {
        return PLOTS;
    }

    /*public Lot getLotAt(Location location) {

    }*/
}
