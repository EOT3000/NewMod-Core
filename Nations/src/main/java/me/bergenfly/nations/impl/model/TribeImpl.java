package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Tribe;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TribeImpl extends AbstractCommunity implements Tribe {

    private static Registry<Community, String> COMMUNITIES;

    private TribeImpl(String name, User leader) {
        this(leader, name, name, System.currentTimeMillis());
    }

    public TribeImpl(User leader, String name, String firstName, long creationTime) {
        super(leader, name, firstName, creationTime, IdUtil.tribeId1(firstName, creationTime));
    }

    public TribeImpl(User leader, String name, String firstName, long creationTime, String id) {
        super(leader, name, firstName, creationTime, id);
    }

    public static TribeImpl tryCreate(String name, User leader) {
        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(name) != null) {
            return null;
        }

        if(leader.getCommunity() != null) {
            return null;
        }

        TribeImpl s = new TribeImpl(name, leader);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s);
        NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return s;
    }

    @Override
    public void sendInfo(CommandSender user) {
        //TODO convert to translation keys
        user.sendMessage(ChatColor.GOLD + "--- [ " + ChatColor.YELLOW + name.replaceAll("_", " ") + ChatColor.GOLD +" ] ---");
        user.sendMessage(ChatColor.DARK_AQUA + "Leader: " + ChatColor.AQUA + leader.getName());
        user.sendMessage(ChatColor.DARK_AQUA + "Nation" + (nation == null ? "less" : ": " + ChatColor.AQUA + nation.getName()));

        String members = "Leader " + leader.getName();

        for(User member : getMembers()) {
            if(!member.equals(leader)) {
                members += (", " + member.getName());
            }
        }

        String membersOnline = "Leader " + leader.getName();

        for(User member : getOnlineMembers()) {
            if(!member.equals(leader)) {
                membersOnline += (", " + member.getName());
            }
        }

        user.sendMessage(ChatColor.DARK_AQUA + "Members: " + ChatColor.AQUA + members);
        user.sendMessage(ChatColor.DARK_AQUA + "Online Members: " + ChatColor.AQUA + membersOnline);
    }
}
