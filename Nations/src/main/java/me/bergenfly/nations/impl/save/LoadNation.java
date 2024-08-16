package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.NationImpl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LoadNation {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static Nation mapToNation(YamlConfiguration configuration, File file) {
        String name = configuration.getString("name");
        String leaderId = configuration.getString("leader");

        String capitalName = configuration.getString("capital");
        String firstName = configuration.getString("firstName");
        int creationTime = configuration.getInt("creationTime", -1);

        Set<Settlement> settlements = configuration.getStringList("settlements").stream().map(api.settlementsRegistry()::get).collect(Collectors.toSet());

        if (name == null) {
            name = "ncnerr_" + Long.toHexString(System.currentTimeMillis());

            int count = 1;

            while (api.nationsRegistry().get(name + "_" + Integer.toHexString(count)) != null) {
                count++;
            }

            name = name + "_" + Integer.toHexString(count);

            logError("Nation in file " + file.getName() + " is invalid (recoverable), missing current name. Current name set to " + name);
        }

        if (firstName == null) {
            firstName = name;

            logError("Nation in file " + file.getName() + " is invalid (recoverable), missing first name. First name set to " + name);
        }

        if (creationTime == -1) {
            creationTime = 0;

            logError("Nation in file " + file.getName() + " is invalid (recoverable), missing creation time. Creation time set to 0");
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

                    logError("Nation in file " + file.getName() + " is invalid (recoverable), missing valid leader (given uuid " + leaderId + "). Leader set to leader of capital ("
                            + leader.getName() + " (" + leader.getUniqueId() + ")" + " of " + capital.getName());
                } else {
                    logError("Nation in file " + file.getName() + " is invalid (unrecoverable), missing leader. Cannot set nation leader to leader of capital because capital name " + capitalName + " is not the name of a settlement. Skipping file");
                    return null;
                }
            } else {
                logError("Nation in file " + file.getName() + " is invalid (unrecoverable), missing leader. Cannot set nation leader to leader of capital because capital name is not set. Skipping file");
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
                    logError("Nation in file " + file.getName() + " is invalid (unrecoverable), file contains no valid towns nor a valid capital (given " + capitalName + "). Skipping file");
                    return null;
                }
            } else {
                //Optional will be present because set contains at least 2 settlements. Because this is a set, at most 1 will be null, meaning the other exists.
                capital = settlements.stream().filter(Objects::nonNull).max(Comparator.comparingInt(a -> a.getMembers().size())).get();
                logError("Nation in file " + file.getName() + " is invalid (recoverable), file contains no valid capital (given " + capitalName + "). Capital set to largest settlement (" + capital.getName() + ")");
            }
        }

        Nation nation = new NationImpl(leader, name, firstName, creationTime, capital);

        for(Settlement settlement : settlements) {
            settlement.setNation(nation);
        }

        return nation;
    }

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }
}
