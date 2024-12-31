package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.organization.Tribe;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.SettlementImpl;
import me.bergenfly.nations.impl.model.TribeImpl;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LoadCommunity {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static Community mapToCommunity(YamlConfiguration configuration, File file) throws IOException {
        String name = configuration.getString("name");
        String leaderId = configuration.getString("leader");

        String firstName = configuration.getString("firstName", null);
        long creationTime = configuration.getLong("creationTime", -1);

        String id = configuration.getString("id", null);

        String finalName = name;

        String type = configuration.getString("type", "error");

        //Mark the file as read so upon next restart, the file is ignored, unless saved to again
        //I don't remember why this was needed, so I'll comment it out for now
        /*configuration.set("read", true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            //An IOException indicates something is wrong with file permissions. If this happens, plugin should be disabled

            logError("Error while loading settlements, on file " + file.getPath() + " . Nations plugin will need to be disabled.");

            throw e;
        }*/

        Set<User> members = configuration.getStringList("members").stream().map((a) -> {
            try {
                return UUID.fromString(a);
            } catch (IllegalArgumentException e) {
                logError("Community (" + finalName + ") in file " + file.getName() + " is invalid (recoverable), contains invalid member uuid. Skipping " + a);

                return null;
            }
        }).map(api.usersRegistry()::get).collect(Collectors.toSet());

        if (name == null) {
            name = "scnerr_" + Long.toHexString(System.currentTimeMillis());

            int count = 1;

            while (api.communitiesRegistry().get(name + "_" + Integer.toHexString(count) + "______") != null) {
                count++;
            }

            name = name + "_" + Integer.toHexString(count) + "______";

            logError("Community in file " + file.getName() + " is invalid (recoverable), missing current name. Current name set to " + name);
        }

        //If id is not null, then lack of first name or creation time is acceptable.
        if(id != null) {
            if (firstName == null) {
                logWarning("WARNING: Community (" + name + ") in file " + file.getName() + " is invalid (potentially recoverable), missing first name. Attempting to recreate first name");

                String nfn = null;

                try {
                    nfn = IdUtil.nameFromId1(id);
                    logWarning("Success: set community's first name to " + nfn);
                    logWarning("---");
                } catch (Exception e) {
                    logError("Unrecoverable error: Could not set community's first name. Skipping community");
                    logError("---");

                    return null;
                }

                firstName = nfn;
            }

            if (creationTime == -1) {
                logWarning("WARNING: Community (" + name + ") in file " + file.getName() + " is invalid (potentially recoverable), missing creation time. Attempting to recreate creation time");

                long nct = -1;

                try {
                    nct = IdUtil.creationTimeFromId1(id);
                    logWarning("Success: set community's creation time to " + nct);
                    logWarning("---");
                } catch (Exception e) {
                    logError("Unrecoverable error: Could not set community's creation time. Skipping community");
                    logError("---");

                    return null;
                }

                creationTime = nct;
            }
        } else { //Otherwise, possibly generate a new id from first name and time?
            boolean valid = true;

            if (firstName == null) {
                logError("Community (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing first name and id. Skipping file");
                valid = false;
            }

            if (creationTime == -1) {
                logError("Community (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing creation time and id. Skipping file");
                valid = false;
            }

            if(!valid) {
                return null;
            }
        }

        UUID leaderUUID = null;

        if (leaderId != null) {
            try {
                leaderUUID = UUID.fromString(leaderId);
            } catch (IllegalArgumentException e) {
                //
            }
        }

        User leader = api.usersRegistry().get(leaderUUID);

        if (leader == null) {
            User next;

            if(members.size() <= 1) {
                next = members.size() == 1 ? members.iterator().next() : null;

                if(next == null) {
                    logError("Community (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), file contains no valid members nor a valid leader (given " + leaderId + "). Skipping file");
                    return null;
                }
            } else {
                //Optional will be present because set contains at least 2 members. Because this is a set, at most 1 will be null, meaning the other exists.
                next = members.stream().filter(Objects::nonNull).findAny().get();
                logError("Community (" + name + ") in file " + file.getName() + " is invalid (recoverable), file contains no valid leader (given " + leaderId + "). Leader set to random member (" + next.getName() + "/" + next.getUniqueId() + ")");
            }

            leader = next;
        }

        if(type.equals("settlement")) {
            if(id == null) {
                id = IdUtil.settlementId1(firstName, creationTime);
            }

            Settlement settlement = new SettlementImpl(leader, name, firstName, creationTime, id);

            for (User user : members) {
                if (user != null) {
                    user.setCommunity(settlement);
                }
            }

            return settlement;
        } else if(type.equals("tribe")) {
            if(id == null) {
                id = IdUtil.tribeId1(firstName, creationTime);
            }

            Tribe tribe = new TribeImpl(leader, name, firstName, creationTime, id);

            for (User user : members) {
                if (user != null) {
                    user.setCommunity(tribe);
                }
            }

            return tribe;
        } else {
            if(id == null) {
                logError("Community (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), file does not contain an id, nor a valid community type (tribe or settlement). Skipping file");

                return null;
            }

            if(id.startsWith("settlement")) {
                Settlement settlement = new SettlementImpl(leader, name, firstName, creationTime, id);

                for (User user : members) {
                    if (user != null) {
                        user.setCommunity(settlement);
                    }
                }

                return settlement;
            }

            if(id.startsWith("tribe")) {
                Tribe tribe = new TribeImpl(leader, name, firstName, creationTime, id);

                for (User user : members) {
                    if (user != null) {
                        user.setCommunity(tribe);
                    }
                }

                return tribe;
            }

            logError("Community (" + name + ") in file " + file.getName() + " is invalid (unrecoverable). Provided id " + id + " is malformed. Skipping file");

            return null;
        }
    }

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }

    private static void logWarning(String warn) {
        api.getLogger().log(Level.WARNING, warn);
    }

    public static void loadCommunities() throws IOException {
        File dir = new File("plugins/Nations/communities");

        if(!dir.exists()) {
            return;
        }

        for(File file : dir.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Community community = mapToCommunity(config, file);

            if(community != null) {
                api.communitiesRegistry().set(community.getName(), community);
                api.permissionHoldersByIdRegistry().set(community.getId(), community);
                api.permissionManager().registerHolder(community, null);
            }
        }
    }
}
