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
import me.bergenfly.nations.impl.model.plot._2x2_Chunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ClaimUtil {
    public static final NationsLandManager manager = NationsPlugin.getInstance().landManager();

    public static ClaimedChunk split(ClaimedChunk chunk, PlotSection section) {
        _2x2_Chunk newChunk = new _2x2_Chunk(chunk.getChunkX(), chunk.getChunkZ(), chunk.getWorld(), section.getAdministrator());

        PlotSection sec = section.cloneAt(newChunk);

        newChunk.setAt(0, 0, (PlotSection) null);
        newChunk.setAt(0, 15, (PlotSection) null);
        newChunk.setAt(15, 0, (PlotSection) null);
        newChunk.setAt(15, 15, (PlotSection) null);

        newChunk.setAt(0, 0, sec);
        newChunk.setAt(0, 15, sec);
        newChunk.setAt(15, 0, sec);
        newChunk.setAt(15, 15, sec);

        manager.replaceChunk(newChunk);

        return newChunk;
    }

    public static boolean tryUnclaimWithChecksAndArgs(User user, LandAdministrator administrator, String[] args) {
        Location location = user.getPlayer().getLocation();

        if(args.length == 0 || args[0].equalsIgnoreCase("chunk")) {
            ClaimedChunk chunk = manager.getClaimedChunkAtLocation(user.getPlayer().getLocation());

            if(manager.chunkIsOnly(chunk, administrator)) {
                manager.unclaimChunk(location.getWorld(), location.getChunk().getX(), location.getChunk().getZ());
                return true;
            }

            user.sendMessage(TranslatableString.translate("nations.general.no_permission"));

            return false;
        }

        if(args[0].equalsIgnoreCase("quarter")) {
            PlotSection section = manager.getPlotSectionAtLocation(location);

            if(section == null) {
                user.sendMessage(TranslatableString.translate("nations.claim.error.not_claimed"));
                return false;
            }

            if(section.getAdministrator() != administrator) {
                user.sendMessage(TranslatableString.translate("nations.general.no_permission"));
                return false;
            }

            ChunkLocation cl = new ChunkLocation(location.getBlockX(), location.getBlockZ());

            ClaimedChunk chunk = section.in();

            if(chunk.getDivision() == 0) {
                chunk = split(section.in(), section);
            }

            if(chunk.getDivision() == 1) {
                chunk.setAt(cl.coordWithinChunkX(), cl.coordWithinChunkZ(), (LandAdministrator) null);

                return true;
            }

            if(chunk.getDivision() > 1) {
                for(int x = cl.minCoordQuarterWithinChunkX(); x < cl.maxCoordQuarterWithinChunkX(); x++) {
                    for(int z = cl.minCoordQuarterWithinChunkZ(); z < cl.maxCoordQuarterWithinChunkZ(); z++) {
                        PlotSection at = chunk.getAt(x, z);

                        if(at == null || at.getAdministrator().equals(administrator)) {
                            user.sendMessage(TranslatableString.translate("nations.general.no_permission"));
                            return false;
                        }
                    }
                }

                for(int x = cl.minCoordQuarterWithinChunkX(); x < cl.maxCoordQuarterWithinChunkX(); x++) {
                    for(int z = cl.minCoordQuarterWithinChunkZ(); z < cl.maxCoordQuarterWithinChunkZ(); z++) {
                        chunk.setAt(x, z, (LandAdministrator) null);
                    }
                }

                return true;
            }
        }

        user.sendMessage("ClaimUtil: unknown argument");
        return false;
    }

    public static boolean tryClaimWithChecksAndArgs(User user, LandAdministrator administrator, String type, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("chunk")) {
            return tryClaimFullChunkWithChecks(user, administrator, type);
        }

        if(args[0].equalsIgnoreCase("quarter")) {
            return tryClaimQuarterWithChecks(user, administrator, type);
        }

        user.sendMessage("ClaimUtil: unknown argument");
        return false;
    }

    public static boolean tryClaimQuarterWithChecks(User user, @Nullable LandAdministrator administrator, String type) {
        Location location = user.getPlayer().getLocation();

        ClaimedChunk chunk = manager.getClaimedChunkAtLocation(location);

        if(chunk != null) {
            if(chunk.getDivision() == 0) {
                user.sendMessage(TranslatableString.translate("nations.command.error.plot.not_split"));
                return false;
            }
        }

        ChunkLocation cl = new ChunkLocation(location.getBlockX(), location.getBlockZ());

        if(administrator != null) {
            switch (adjacentToSettlementOrNationQuarter(cl, location.getWorld(), administrator)) {
                case -1: {
                    user.sendMessage(TranslatableString.translate("nations.claim.error.not_adjacent", type));
                    return false;
                }
                case -2: {
                    user.sendMessage(TranslatableString.translate("nations.claim.error.not_adjacent", "capital settlement"));
                    return false;
                }
            }
        }

        boolean result = manager.tryClaimQuarterAtLocationOtherwiseFail(location, administrator);

        if(!result) {
            if(administrator != null) {
                user.sendMessage(TranslatableString.translate("nations.claim.error.already_claimed", "quarter"));
            }
            return false;
        }

        return true;
    }

    public static boolean tryClaimFullChunkWithChecks(User user, @Nullable LandAdministrator administrator, String type) {
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

        boolean result = manager.tryClaimFullChunkOtherwiseFail(chunk, administrator);

        if(!result) {
            user.sendMessage(TranslatableString.translate("nations.claim.error.already_claimed", "chunk"));
            return false;
        }

        return true;
    }

    //Checks:
    // If nation, then must be next to an existing **nation** claim (cannot be settlement claim in nation)
    // However, if nation has no nation claims yet, then must be adjacent to capital settlement claim
    //
    // If settlement, then must be next to existing settlement claim
    public static int adjacentToSettlementOrNation(int chunkX, int chunkZ, World world, @Nullable LandAdministrator administrator) {
        if(administrator instanceof Nation n && n.getNationLand().isEmpty()) {
            for(int i = 0; i < 4; i++) {
                BlockFace face = BlockFace.values()[i];

                ClaimedChunk chunk = getRelative(chunkX, chunkZ, world, face);

                if(isChunkTouchingSpecific(chunk, n.getCapital(), face.getOppositeFace())) {
                    return 1; //Good
                }
            }

            return -2; //Not touching capital
        } else {
            if(administrator instanceof Settlement && administrator.getLand().isEmpty()) return 1;

            for(int i = 0; i < 4; i++) {
                BlockFace face = BlockFace.values()[i];

                ClaimedChunk chunk = getRelative(chunkX, chunkZ, world, face);

                if(isChunkTouchingSpecific(chunk, administrator, face.getOppositeFace())) {
                    return 1; //Good
                }
            }

            return -1; //Not touching itself
        }
    }

    public static int adjacentToSettlementOrNationQuarter(ChunkLocation cl, World world, LandAdministrator administrator) {
        if(administrator instanceof Nation n && n.getNationLand().isEmpty()) {
            if(isAreaTouchingSpecific(cl.minCoordQuarterX(), cl.minCoordQuarterZ(), cl.maxCoordQuarterX(), cl.maxCoordQuarterZ(), world, n.getCapital())) {
                return 1; //Good
            }

            return -2; //Not touching capital
        } else {
            if(administrator instanceof Settlement && administrator.getLand().isEmpty()) return 1;

            if(isAreaTouchingSpecific(cl.minCoordQuarterX(), cl.minCoordQuarterZ(), cl.maxCoordQuarterX(), cl.maxCoordQuarterZ(), world, administrator)) {
                return 1; //Good
            }

            return -1; //Not touching itself
        }
    }

    public static ClaimedChunk getRelative(int chunkX, int chunkZ, World world, BlockFace face) {
        return manager.getClaimedChunkAtChunk(world, face.getModX()+chunkX, face.getModZ()+chunkZ);
    }

    private static boolean isAreaTouchingSpecific(int xMin, int zMin, int xMax, int zMax, World world, LandAdministrator administrator) {
        for(int x = xMin; x < xMax; x++) {
            int z = zMin-1;

            PlotSection section = manager.getPlotSectionAtLocation(x, z, world);

            if(section != null && section.getAdministrator().equals(administrator)) {
                return true;
            }

            z = zMax+1;

            section = manager.getPlotSectionAtLocation(x, z, world);

            if(section != null && section.getAdministrator().equals(administrator)) {
                return true;
            }
        }

        for(int z = zMin; z < zMax; z++) {
            int x = xMin-1;

            PlotSection section = manager.getPlotSectionAtLocation(x, z, world);

            if(section != null && section.getAdministrator().equals(administrator)) {
                return true;
            }

            x = xMax+1;

            section = manager.getPlotSectionAtLocation(x, z, world);

            if(section != null && section.getAdministrator().equals(administrator)) {
                return true;
            }
        }

        return false;
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
