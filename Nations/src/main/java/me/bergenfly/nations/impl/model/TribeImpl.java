package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Tribe;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.command.CommandSender;

public class TribeImpl extends AbstractCommunity implements Tribe {

    private static Registry<Community, String> COMMUNITIES;

    private TribeImpl(String name, User leader) {
        this(leader, name, name, System.currentTimeMillis());
    }

    public TribeImpl(User leader, String name, String firstName, long creationTime) {
        super(leader, name, firstName, creationTime);
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

        return s;
    }

    @Override
    public void sendInfo(CommandSender user) {

    }
}
