package me.bergenfly.nations.util;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ClaimUtil2 {

    static NationsPlugin plugin = NationsPlugin.getInstance();
    static NationsLandManager manager = plugin.landManager();


    public static int tryClaimSettlementWithClaimChecks(User user, Player player) {
        Chunk chunk = player.getLocation().getChunk();

        return tryClaimSettlementWithClaimChecks(user.getCommunity(), chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public static int tryClaimSettlementWithClaimChecks(Town town, World w, int chunkX, int chunkZ) {
        if(town.getLand().isEmpty()) {
            //
        } else if(town.getLand().size() < 5) {
            if(getDirectlyAdjacent(town, false, w, chunkX, chunkZ) == 0) {
                return -1; //<5; 1 directly adjacent
            }
        } else {
            if(getAdjacent(town, false, w, chunkX, chunkZ) < 3) {
                return -2; //>=5; 3 adjacent or diagonal
            }
        }

        if(manager.getClaimedChunkAtChunk(w, chunkX, chunkZ) != null) {
            return -3; //already claimed
        }

        if(town.getMaxChunks() <= town.getLand().size()) {
            return -4; //not enough chunks
        }

        manager.tryClaimFullChunkOtherwiseFail(w, chunkX, chunkZ, town);

        return 1;
    }

    public static int getAdjacent(LandAdministrator admin, boolean strict, World w, int chunkX, int chunkZ) {
        NationsPlugin plugin = NationsPlugin.getInstance();
        NationsLandManager manager = plugin.landManager();

        int count = 0;

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                if(x == 0 && z == 0) {
                    continue;
                }



                if(admin.equals(adminAt(w, chunkX+x, chunkZ+z))) {
                    count++;
                }
            }
        }

        return count;
    }

    public static int getDirectlyAdjacent(LandAdministrator admin, boolean strict, World w, int chunkX, int chunkZ) {
        NationsPlugin plugin = NationsPlugin.getInstance();
        NationsLandManager manager = plugin.landManager();

        int count = 0;

        if(admin.equals(adminAt(w, chunkX+1, chunkZ))) count++;

        if(admin.equals(adminAt(w, chunkX, chunkZ+1))) count++;

        if(admin.equals(adminAt(w, chunkX-1, chunkZ))) count++;

        if(admin.equals(adminAt(w, chunkX, chunkZ-1))) count++;

        return count;
    }

    private static LandAdministrator adminAt(World w, int chunkX, int chunkZ) {
        ClaimedChunk chunk = manager.getClaimedChunkAtChunk(w, chunkX, chunkZ);

        if(chunk == null) {
            return null;
        }

        return chunk.getStorage().getAdminAt(0,0);
    }
}
