package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.NationImpl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LoadNation {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    //TODO: make these nicer by sharing the same code between all the loaders.

    public static Nation mapToNation(YamlConfiguration configuration, File file) throws IOException {
        String name = configuration.getString("name");
        String leaderId = configuration.getString("leader");

        String capitalName = configuration.getString("capital");
        String firstName = configuration.getString("firstName", null);
        int creationTime = configuration.getInt("creationTime", -1);

        Set<Settlement> settlements = configuration.getStringList("settlements").stream().map(api.permissionHoldersByIdRegistry()::get).map((a) -> (Settlement) a).collect(Collectors.toSet());

        String id = configuration.getString("id", null);

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

        if (name == null) {
            name = "ncnerr_" + Long.toHexString(System.currentTimeMillis());

            int count = 1;

            while (api.nationsRegistry().get(name + "_" + Integer.toHexString(count)) != null) {
                count++;
            }

            name = name + "_" + Integer.toHexString(count);

            logError("Nation (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing current name. Current name set to " + name);
        }

        //If id is not null, then lack of first name or creation time is acceptable.
        if(id != null) {
            if (firstName == null) {
                logWarning("WARNING: Nation (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing first name. First name will not be set");
            }

            if (creationTime == -1) {
                logWarning("WARNING: Nation (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing creation time. Creation time will not be set");
            }
        } else { //Otherwise, possibly generate a new id from first name and time?
            boolean valid = true;

            if (firstName == null) {
                logError("Nation (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing first name and id. Skipping file");
                valid = false;
            }

            if (creationTime == -1) {
                logError("Nation (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing creation time and id. Skipping file");
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

        Settlement capital = null;

        User leader = api.usersRegistry().get(leaderUUID);

        if (leader == null) {
            if (capitalName != null) {
                capital = api.settlementsRegistry().get(capitalName);

                if(capital != null) {
                    leader = capital.getLeader();

                    logError("Nation (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing valid leader (given uuid " + leaderId + "). Leader set to leader of capital ("
                            + leader.getName() + " (" + leader.getUniqueId() + ")" + " of " + capital.getName());
                } else {
                    logError("Nation (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing leader. Cannot set nation leader to leader of capital because capital name " + capitalName + " is not the name of a settlement. Skipping file");
                    return null;
                }
            } else {
                logError("Nation (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing leader. Cannot set nation leader to leader of capital because capital name is not set. Skipping file");
                return null;
            }
        }

        if(capitalName != null) {
            capital = api.settlementsRegistry().get(capitalName);
        }

        if(capital == null) {
            if(settlements.size() <= 1) {
                capital = settlements.size() == 1 ? settlements.iterator().next() : null;

                if(capital == null) {
                    logError("Nation (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), file contains no valid towns nor a valid capital (given " + capitalName + "). Skipping file");
                    return null;
                }
            } else {
                //Optional will be present because set contains at least 2 settlements. Because this is a set, at most 1 will be null, meaning the other exists.
                capital = settlements.stream().filter(Objects::nonNull).max(Comparator.comparingInt(a -> a.getMembers().size())).get();
                logError("Nation (" + name + ") in file " + file.getName() + " is invalid (recoverable), file contains no valid capital (given " + capitalName + "). Capital set to largest settlement (" + capital.getName() + ")");
            }
        }

        Nation nation = new NationImpl(leader, name, firstName, creationTime, capital, id);

        for(Settlement settlement : settlements) {
            if(settlement != null) {
                settlement.setNation(nation);
            }
        }

        return nation;
    }

    public static void loadNations() throws IOException {
        File dir = new File("plugins/Nations/nations");

        if(!dir.exists()) {
            return;
        }

        for(File file : dir.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Nation nation = mapToNation(config, file);

            if(nation != null) {
                api.nationsRegistry().set(nation.getName(), nation);
                api.permissionHoldersByIdRegistry().set(nation.getId(), nation);
            }
        }
    }

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }

    private static void logWarning(String warn) {
        api.getLogger().log(Level.WARNING, warn);
    }
}
