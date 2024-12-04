package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Led;

public abstract class AbstractLedPlayerGroup extends AbstractPlayerGroup implements Led {
    private User leader;

    @Override
    public User getLeader() {
        return leader;
    }

    @Override
    public void setLeader(User user) {
        this.leader = user;

        addMember(user);
    }

    //TODO: remove leader
}
