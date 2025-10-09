package me.bergenfly.nations.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Rank;
import me.bergenfly.nations.api.permission.DefaultNationPermission;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.model.RankImpl;
import me.bergenfly.nations.util.IdUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class RankSaverLoader {
    public static Map<String, Object> rankToYaml(Rank rank) {
        Map<String, Object> ret = new HashMap<>();

        ret.put("rankName", rank.getName());
        ret.put("rankId", rank.getId());
        ret.put("rankCreationTime", rank.getCreationTime());
        ret.put("rankLeader", rank.getLeader() != null ? rank.getLeader().getUniqueId().toString() : "null");
        ret.put("rankMembers", rank.getMembers().stream().filter(Objects::nonNull).map(User::getUniqueId).map(UUID::toString).collect(Collectors.toList()));
        ret.put("rankPermissions", rank.getPermissions().stream().filter(Objects::nonNull).map(NationPermission::getKey).collect(Collectors.toList()));

        return ret;
    }

    public static List<Map<String, Object>> ranksToYaml(Nation nation) {
        List<Map<String, Object>> ret = new ArrayList<>();

        for(Rank rank : nation.getRanks()) {
            Map<String,Object> map = rankToYaml(rank);

            ret.add(map);
        }

        return ret;
    }

    public static Rank rankFromYaml(Map<?,?> map, Nation nation, File file) {
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
            logWarning("Error loading rank in file " + file.getName() + " (" + nation.getName() + "). Missing id. Attempting recovery. Dumping data:");
            logWarning(map.toString());

            if(!(rName_ instanceof String)) {
                logError("Could not recover, missing rank name. Skipping this rank.");
                logError("---");
                return null;
            }

            if(!(rCreationTime_ instanceof Long)) {
                logError("Could not recover, missing rank creation time. Skipping this rank.");
                logError("---");
                return null;
            }

            id = IdUtil.rankId1((String) rName_, nation.getId(), (Long) rCreationTime_);

            logInfo("Successfully reconstructed id for rank " + rName_ + " in file " + file.getName());
            logInfo("Reconstructed id: " + id);
            logInfo("---");
        } else {
            id = (String) rId_;
        }

        if(!(rName_ instanceof String)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Missing name. Trying to determine from id");

            name = IdUtil.nameFromId1(id);

            logWarning("Created new name: " + name);
            logInfo("---");
        } else {
            name = (String) rName_;
        }

        if(!(rCreationTime_ instanceof Long)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Missing creation time. Trying to determine from id");

            creationTime = IdUtil.creationTimeFromId1(id);

            logWarning("Created new time: " + creationTime);
            logInfo("---");
        } else {
            creationTime = (Long) rCreationTime_;
        }

        if(!(rLeader_ instanceof String)) {
            logWarning("Recoverable error loading rank in file " + file.getName() + " (" + nation.getName() + "). Missing leader. Leaving leader unset.");

            leader = null;
        } else if(!rLeader_.equals("null")) {
            try {
                UUID uuid = UUID.fromString((String) rLeader_);

                leader = api.usersRegistry().get(uuid);

                if(leader == null) { //Only check for null leader, we don't care too much if leader isn't in nation for example
                    logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Leader UUID (" + rLeader_ + ") is not a valid user. Leaving leader unset.");
                }
            } catch (IllegalArgumentException e) {
                logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Given leader UUID " + rLeader_ + " is not a valid UUID. Leaving leader unset.");

                leader = null;
            }
        } else {
            leader = null;
        }

        Rank rank = new RankImpl(name, leader, nation, creationTime, id);

        if(!(rMembers_ instanceof List<?>)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Missing members. Leaving members unset.");
        } else {
            for(Object object : ((List<?>) rMembers_)) {
                if(object instanceof String) {
                    try {
                        UUID uuid = UUID.fromString((String) object);

                        User member = api.usersRegistry().get(uuid);

                        if(member == null) { //Only check for null leader, we don't care too much if leader isn't in nation for example
                            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Member UUID (" + rLeader_ + ") is not a valid user. Skipping member.");
                        } else {
                            rank.addMember(member);
                        }
                    } catch (IllegalArgumentException e) {
                        logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Given member UUID " + object + "is not a valid UUID. Skipping member.");
                    }
                } else {
                    logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Given member UUID " + object + "is not a valid UUID. Skipping member.");
                }
            }
        }

        if(!(rPermissions_ instanceof List<?>)) {
            logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Missing permissions. Leaving permissions unset.");
        } else {
            for(Object object : ((List<?>) rPermissions_)) {
                if(object instanceof String s) {
                    try {
                        DefaultNationPermission permission = DefaultNationPermission.valueOf(s);

                        rank.setPermission(permission);
                    } catch (IllegalArgumentException e) {
                        logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Given permission " + object + "is not a nation permission. Skipping permission.");
                    }
                } else {
                    logWarning("Recoverable error loading rank " + id + " in file " + file.getName() + " (" + nation.getName() + "). Given permission " + object + "is not a nation permission. Skipping permission.");
                }
            }
        }

        return rank;
    }

    public static void loadRanks(Nation nation, List<Map<?,?>> ranks, File file) {
        for(Map<?,?> rankMap : ranks) {
            Rank rank = rankFromYaml(rankMap, nation, file);

            if(rank != null) {
                nation.addRank(rank);
            }
        }
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
