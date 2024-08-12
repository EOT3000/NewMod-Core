package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

public interface Led {
    User getLeader();

    void setLeader(User user);
}
