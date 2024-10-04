package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.SettlementImpl;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LoadSettlement {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static Settlement mapToSettlement(YamlConfiguration configuration, File file) throws IOException {
        String name = configuration.getString("name");
        String leaderId = configuration.getString("leader");

        String firstName = configuration.getString("firstName", null);
        int creationTime = configuration.getInt("creationTime", -1);

        String id = configuration.getString("id", null);

        String finalName = name;

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
                logError("Settlement (" + finalName + ") in file " + file.getName() + " is invalid (recoverable), contains invalid member uuid. Skipping " + a);

                return null;
            }
        }).map(api.usersRegistry()::get).collect(Collectors.toSet());

        if (name == null) {
            name = "scnerr_" + Long.toHexString(System.currentTimeMillis());

            int count = 1;

            while (api.nationsRegistry().get(name + "_" + Integer.toHexString(count)) != null) {
                count++;
            }

            name = name + "_" + Integer.toHexString(count);

            logError("Settlement in file " + file.getName() + " is invalid (recoverable), missing current name. Current name set to " + name);
        }

        //If id is not null, then lack of first name or creation time is acceptable.
        if(id != null) {
            if (firstName == null) {
                logWarning("WARNING: Settlement (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing first name. First name will not be set");
            }

            if (creationTime == -1) {
                logWarning("WARNING: Settlement (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing creation time. Creation time will not be set");
            }
        } else { //Otherwise, possibly generate a new id from first name and time?
            boolean valid = true;

            if (firstName == null) {
                logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing first name and id. Skipping file");
                valid = false;
            }

            if (creationTime == -1) {
                logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), missing creation time and id. Skipping file");
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
                    logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (unrecoverable), file contains no valid members nor a valid leader (given " + leaderId + "). Skipping file");
                    return null;
                }
            } else {
                //Optional will be present because set contains at least 2 settlements. Because this is a set, at most 1 will be null, meaning the other exists.
                next = members.stream().filter(Objects::nonNull).findAny().get();
                logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (recoverable), file contains no valid leader (given " + leaderId + "). Leader set to random member (" + next.getName() + "/" + next.getUniqueId() + ")");
            }

            leader = next;
        }

        Settlement settlement = new SettlementImpl(leader, name, firstName, creationTime, id);

        for(User user : members) {
            if(user != null) {
                user.setSettlement(settlement);
            }
        }

        return settlement;
    }

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }

    private static void logWarning(String warn) {
        api.getLogger().log(Level.WARNING, warn);
    }

    public static void loadSettlements() throws IOException {
        File dir = new File("plugins/Nations/settlements");

        if(!dir.exists()) {
            return;
        }

        for(File file : dir.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Settlement settlement = mapToSettlement(config, file);

            if(settlement != null) {
                api.settlementsRegistry().set(settlement.getName(), settlement);
            }
        }
    }
}
