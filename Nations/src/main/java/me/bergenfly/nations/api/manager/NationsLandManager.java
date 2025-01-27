package me.bergenfly.nations.api.manager;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.model.plot._1x1_Chunk;
import me.bergenfly.nations.impl.model.plot._2x2_Chunk;
import me.bergenfly.nations.impl.registry.RegistryImpl;
import me.bergenfly.nations.impl.util.ChunkLocation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Iterator;

public class NationsLandManager {
    private Registry<ClaimedChunk, Integer> PLOTS = new RegistryImpl<>(ClaimedChunk.class);

    public NationsLandManager() {
        Plots.init();
    }

    public void replaceChunk(ClaimedChunk neew) {
        int id = Plots.getLocationId(neew.getChunkX(), neew.getChunkZ(), neew.getWorld());

        if(PLOTS.get(id) != null) {
            PLOTS.get(id).unclaim();
        }

        PLOTS.set(id, neew);
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
        simplifyAt(PLOTS.get(Plots.getLocationId(chunkX, chunkZ, w)));
        return PLOTS.get(Plots.getLocationId(chunkX, chunkZ, w));
    }

    public void unclaimChunk(World w, int chunkX, int chunkZ) {
        int id = Plots.getLocationId(chunkX, chunkZ, w);
        PLOTS.get(id).unclaim();
        PLOTS.set(id, null);
    }

    public boolean tryClaimQuarterAtLocationOtherwiseFail(Location location, LandAdministrator administrator) {
        ClaimedChunk chunk = getClaimedChunkAtLocation(location);

        ChunkLocation cl = new ChunkLocation(location.getBlockX(), location.getBlockZ());

        if(chunk != null) {
            if(chunk.getDivision() == 0) {
                simplifyAt(chunk);
                if(getClaimedChunkAtLocation(location) != null) {
                    return false;
                }
            } else if(chunk.getDivision() == 1) {
                PlotSection section = getPlotSectionAtLocation(location);

                if(section != null) {
                    return false;
                }

                chunk.setAt(cl.coordWithinChunkX(), cl.coordWithinChunkZ(), administrator);

                administrator.addLand(chunk.getAt(cl.coordWithinChunkX(), cl.coordWithinChunkZ()));
            } else if(chunk.getDivision() > 1) {
                //Check, are all sixteenths/sixtyfourths, etc unclaimed?

                for(int x = cl.minCoordQuarterWithinChunkX(); x < cl.maxCoordQuarterWithinChunkX(); x++) {
                    for(int z = cl.minCoordQuarterWithinChunkZ(); z < cl.maxCoordQuarterWithinChunkZ(); z++) {
                        if(chunk.getAt(x, z) != null) {
                            return false;
                        }
                    }
                }

                //If yes, set all sixteenths/sixtyfourths etc within the quarter to the administrator

                for(int x = cl.minCoordQuarterWithinChunkX(); x < cl.maxCoordQuarterWithinChunkX(); x++) {
                    for(int z = cl.minCoordQuarterWithinChunkZ(); z < cl.maxCoordQuarterWithinChunkZ(); z++) {
                        chunk.setAt(x, z, administrator);

                        administrator.addLand(chunk.getAt(x, z));
                    }
                }

                return true;
            } else {
                return false;
            }
        }

        _2x2_Chunk newChunk = new _2x2_Chunk(location.getChunk().getX(), location.getChunk().getZ(), location.getWorld(), null, null, null, null);

        newChunk.setAt(cl.coordWithinChunkX(), cl.coordWithinChunkZ(), administrator);

        administrator.addLand(newChunk.getAt(cl.coordWithinChunkX(), cl.coordWithinChunkZ()));

        PLOTS.set(Plots.getLocationId(newChunk), newChunk);

        return true;
    }

    public boolean tryClaimFullChunkAtLocationOtherwiseFail(Location location, LandAdministrator administrator) {
        return tryClaimFullChunkOtherwiseFail(location.getChunk(), administrator);
    }

    public boolean tryClaimFullChunkOtherwiseFail(Chunk chunk, LandAdministrator administrator) {
        return tryClaimFullChunkOtherwiseFail(chunk.getWorld(), chunk.getX(), chunk.getZ(), administrator);
    }

    public boolean tryClaimFullChunkOtherwiseFail(World w, int chunkX, int chunkZ, LandAdministrator administrator) {
        int id = Plots.getLocationId(chunkX, chunkZ, w);

        ClaimedChunk chunk = PLOTS.get(id);

        if(chunk != null) {
            simplifyAt(chunk);

            if (PLOTS.get(id) != null) {
                return false;
            }
        }

        _1x1_Chunk c = new _1x1_Chunk(chunkX, chunkZ, w, administrator);

        administrator.addLand(c.getAt(0,0));

        PLOTS.set(id, c);

        return true;
    }

    private void simplifyAt(ClaimedChunk chunk) {
        if(chunk.getSections(false).length == 0) {
            PLOTS.set(Plots.getLocationId(chunk), null);
        }
    }

    public PlotSection getPlotSectionAtLocation(Location location) {
        ClaimedChunk chunk = getClaimedChunkAtChunk(location.getChunk());
        return chunk == null ? null : chunk.getAt(location.getBlockX()-chunk.getChunkX()*16, location.getBlockZ()-chunk.getChunkZ()*16);
    }

    public PlotSection getPlotSectionAtLocation(int x, int z, World world) {
        ClaimedChunk chunk = getClaimedChunkAtChunk(world, x, z);
        return chunk == null ? null : chunk.getAt(x-chunk.getChunkX()*16, z-chunk.getChunkZ()*16);
    }

    public Registry<ClaimedChunk, Integer> getPLOTS() {
        return PLOTS;
    }
}
