package me.bergenfly.nations.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SaveCommunity {
    public static YamlConfiguration communityToMap(Community community) {
        YamlConfiguration communitySave = new YamlConfiguration();

        communitySave.set("name", community.getName());
        communitySave.set("id", community.getId());
        communitySave.set("leader", community.getLeader().getUniqueId().toString());
        if(community.getFirstName() != null) communitySave.set("firstName", community.getFirstName());
        if(community.getCreationTime() != -1) communitySave.set("creationTime", community.getCreationTime());
        communitySave.set("members", community.getMembers().stream().filter(Objects::nonNull).map(User::getUniqueId).map(UUID::toString).collect(Collectors.toList()));
        communitySave.set("type", community.isSettlement() ? "settlement" : "tribe");

        return communitySave;
    }

    public static void saveCommunities() {
        for(Community community : NationsPlugin.getInstance().communitiesRegistry().list()) {
            try {
                communityToMap(community).save(new File("plugins/Nations/communities/" + community.getId() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save community " + community.getName() + "(originally " + community.getFirstName() + ")");
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
