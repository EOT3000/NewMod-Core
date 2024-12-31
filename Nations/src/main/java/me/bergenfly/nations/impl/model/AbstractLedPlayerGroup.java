package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Led;

public abstract class AbstractLedPlayerGroup extends AbstractPlayerGroup implements Led {
    protected User leader;

    @Override
    public User getLeader() {
        return leader;
    }

    @Override
    public void setLeader(User user) {
        if(canLackLeader()) {
            this.leader = user;

            if(user != null) {
                addMember(user);
            }
        } else {
            if (user != null) {
                this.leader = user;

                addMember(user);
            }
        }
    }

    protected boolean canLackLeader() {
        return false;
    }

    //TODO: remove leader
}
