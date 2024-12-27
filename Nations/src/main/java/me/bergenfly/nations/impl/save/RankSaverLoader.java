package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Rank;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class RankSaverLoader {
    public Map<String, Object> rankToYaml(Rank rank) {
        Map<String, Object> ret = new HashMap<>();

        ret.put("rankName", rank.getName());
        ret.put("rankId", rank.getId());
        ret.put("rankCreationTime", rank.getCreationTime());
        ret.put("rankLeader", rank.getLeader() != null ? rank.getLeader().getUniqueId().toString() : "null");
        ret.put("rankMembers", rank.getMembers().stream().filter(Objects::nonNull).map(User::getUniqueId).map(UUID::toString).collect(Collectors.toList()));
        ret.put("rankPermissions", rank.getPermissions().stream().filter(Objects::nonNull).map(NationPermission::getKey).collect(Collectors.toList()));

        return ret;
    }

    public Rank rankFromYaml(Map<?,?> map, String nationId, File file, String nationName) {
        Object rName_ = map.get("rankName");
        Object rId_ = map.get("rankId");
        Object rLeader_ = map.get("rankLeader");
        Object rMembers_ = map.get("rankMembers");
        Object rPermissions_ = map.get("rankPermissions");
        Object rCreationTime_ = map.get("rankCreationTime");

        String id;
        String name;
        long creationTime;
        User leader;

        if(!(rId_ instanceof String)) {
            logWarning("Error loading rank in file " + file.getName() + " (" + nationName + "). Missing id. Attempting recovery. Dumping data:");
            logWarning(map.toString());

            if(!(rName_ instanceof String)) {
                logError("Could not recover, missing rank name. Skipping this rank.");
                logError("---");
                return null;
            }

            if(!(rCreationTime_ instanceof Long)) {
                logError("Could not recover, missing creation time. Skipping this rank.");
                logError("---");
                return null;
            }

            id = IdUtil.rankId1((String) rName_, nationId, (Long) rCreationTime_);

            logInfo("Successfully reconstructed id for rank " + rName_ + " in file " + file.getName());
            logInfo("Reconstructed id: " + id);
            logInfo("---");
        } else {
            id = (String) rId_;
        }

        if(!(rName_ instanceof String)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nationName + "). Missing name. Trying to determine from id");

            name = IdUtil.nameFromId1(id);

            logWarning("Created new name: " + name);
            logInfo("---");
        } else {
            name = (String) rName_;
        }

        if(!(rCreationTime_ instanceof Long)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nationName + "). Missing creation time. Trying to determine from id");

            creationTime = IdUtil.creationTimeFromId1(id);

            logWarning("Created new time: " + creationTime);
            logInfo("---");
        } else {
            creationTime = (Long) rCreationTime_;
        }

        if(!(rLeader_ instanceof String)) {
            logWarning("Recoverable error loading rank in file " + file.getName() + " (" + nationName + "). Missing leader. Leaving leader unset.");

            leader = null;
        } else if(!rLeader_.equals("null")) {
            try {
                UUID uuid = UUID.fromString((String) rLeader_);

                leader = api.usersRegistry().get(uuid);

                if(leader == null) { //Only check for null leader, we don't care too much if leader isn't in nation for example
                    logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nationName + "). Leader UUID (" + rLeader_ + ") is not a valid user. Leaving leader unset.");
                }
            } catch (IllegalArgumentException e) {
                logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nationName + "). Missing leader. Leaving leader unset.");

                leader = null;
            }
        } else {
            leader = null;
        }

        if(!(rMembers_ instanceof List<?>)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nationName + "). Missing members. Leaving members unset.");
        }

        return null;
    }

    private static final NationsPlugin api = NationsPlugin.getInstance();

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }

    private static void logWarning(String warn) {
        api.getLogger().log(Level.WARNING, warn);
    }

    private static void logInfo(String info) {
        api.getLogger().log(Level.INFO, info);
    }
}
