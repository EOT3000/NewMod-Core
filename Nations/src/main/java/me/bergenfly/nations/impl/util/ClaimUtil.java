package me.bergenfly.nations.impl.util;

import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ClaimUtil {
    public static final NationsLandManager manager = NationsPlugin.getInstance().landManager();

    public static boolean tryClaimWithChecks(User user, LandAdministrator administrator, String type) {
        Chunk chunk = user.getPlayer().getLocation().getChunk();

        switch(adjacentToSettlementOrNation(chunk.getX(), chunk.getZ(), chunk.getWorld(), administrator)) {
            case -1: {
                user.sendMessage(TranslatableString.translate("nations.claim.error.not_adjacent", type));
                return false;
            }
            case -2: {
                user.sendMessage(TranslatableString.translate("nations.claim.error.not_adjacent", "capital settlement"));
                return false;
            }
        }

        boolean result = manager.tryClaimChunk(chunk, administrator);

        if(!result) {
            user.sendMessage(TranslatableString.translate("nations.claim.error.already_claimed"));
            return false;
        }

        return true;
    }

    //Checks:
    // If nation, then must be next to an existing **nation** claim (cannot be settlement claim in nation)
    // However, if nation has no nation claims yet, then must be adjacent to capital settlement claim
    //
    // If settlement, then must be next to existing settlement claim
    public static int adjacentToSettlementOrNation(int chunkX, int chunkZ, World world, LandAdministrator administrator) {
        if(administrator instanceof Nation n && n.getNationLand().isEmpty()) {
            for(int i = 0; i < 4; i++) {
                BlockFace face = BlockFace.values()[i];

                ClaimedChunk chunk = getRelative(chunkX, chunkZ, world, face);

                if(isChunkTouchingSpecific(chunk, n.getCapital(), face.getOppositeFace())) {
                    return 1;
                }
            }

            return -2;
        } else {
            for(int i = 0; i < 4; i++) {
                BlockFace face = BlockFace.values()[i];

                ClaimedChunk chunk = getRelative(chunkX, chunkZ, world, face);

                if(isChunkTouchingSpecific(chunk, administrator, face.getOppositeFace())) {
                    return 1;
                }
            }

            return -1;
        }
    }

    public static ClaimedChunk getRelative(int chunkX, int chunkZ, World world, BlockFace face) {
        return manager.getClaimedChunkAtChunk(world, face.getModX()+chunkX, face.getModZ()+chunkZ);
    }

    public static boolean isChunkTouchingSpecific(ClaimedChunk chunk, LandAdministrator administrator, BlockFace fromThe) {
        if(chunk == null) {
            return false;
        }

        for(int i = 0; i < 16; i++) {
            int x = fromThe.getModZ()*i+(int) (7.5 + 7.5*fromThe.getModX());
            int z = fromThe.getModX()*i+(int) (7.5 + 7.5*fromThe.getModZ());

            PlotSection section = chunk.getAt(x,z);

            if(section != null) {
                if(section.getAdministrator().equals(administrator)) {
                    return true;
                }
            }
        }

        return false;
    }
}
