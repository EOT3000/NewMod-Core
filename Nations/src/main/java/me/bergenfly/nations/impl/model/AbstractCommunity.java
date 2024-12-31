package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.util.IdUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommunity extends AbstractPlayerGroup implements Community {

    private static Registry<Community, String> COMMUNITIES;

    protected final String firstName;
    protected final long creationTime;

    protected User leader;

    protected String name;

    protected Nation nation;

    protected Set<User> invitations = new HashSet<>();

    protected final String id;

    //TODO figure out what to do with constructor accessiblity
    public AbstractCommunity(User leader, String name, String firstName, long creationTime, String id) {
        this.leader = leader;
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public User getLeader() {
        return leader;
    }

    @Override
    public void setLeader(User leader) {
        this.leader = leader;
    }

    @Override
    public Nation getNation() {
        return nation;
    }

    @Override
    public void setNation(Nation nation) {
        this.nation = nation;

        if(this.nation != null) {
            this.nation.removeCommunity(this);
        }

        if(nation != null) {
            nation.addCommunity(this);
        }

        this.nation = nation;
    }

    @Override
    public boolean setName(String newName) {
        String oldName = this.name;

        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(newName) != null) {
            return false;
        }

        COMMUNITIES.set(oldName, null);

        this.name = newName;

        COMMUNITIES.set(newName, this);

        NationsPlugin.getInstance().permissionManager().registerHolder(this, oldName);

        return true;
    }

    public String getFirstName() {
        return firstName;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void addInvitation(User user) {
        invitations.add(user);
    }

    @Override
    public Set<User> getInvitations() {
        return new HashSet<>(invitations);
    }
}
