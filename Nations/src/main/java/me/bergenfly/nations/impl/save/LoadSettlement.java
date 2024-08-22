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

        String firstName = configuration.getString("firstName");
        int creationTime = configuration.getInt("creationTime", -1);

        String finalName = name;
        Set<User> members = configuration.getStringList("members").stream().map((a) -> {
            try {
                return UUID.fromString(a);
            } catch (IllegalArgumentException e) {
                logError("Settlement (" + finalName + ") in file " + file.getName() + " is invalid (recoverable), contains invalid member uuid. Skipping " + a);

                return null;
            }
        }).map(api.usersRegistry()::get).collect(Collectors.toSet());

        //Mark the file as read so upon next restart, the file is ignored, unless saved to again
        configuration.set("read", true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            //An IOException indicates something is wrong with file permissions. If this happens, plugin should be disabled

            logError("Error while loading settlements, on file " + file.getPath() + " . Nations plugin will need to be disabled.");

            throw e;
        }

        if (name == null) {
            name = "scnerr_" + Long.toHexString(System.currentTimeMillis());

            int count = 1;

            while (api.nationsRegistry().get(name + "_" + Integer.toHexString(count)) != null) {
                count++;
            }

            name = name + "_" + Integer.toHexString(count);

            logError("Settlement in file " + file.getName() + " is invalid (recoverable), missing current name. Current name set to " + name);
        }

        if (firstName == null) {
            firstName = name;

            logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing first name. First name set to " + name);
        }

        if (creationTime == -1) {
            creationTime = 0;

            logError("Settlement (" + name + ") in file " + file.getName() + " is invalid (recoverable), missing creation time. Creation time set to 0");
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

        Settlement settlement = new SettlementImpl(leader, name, firstName, creationTime);

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
}
