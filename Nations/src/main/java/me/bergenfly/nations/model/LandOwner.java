package me.bergenfly.nations.model;

import me.bergenfly.nations.serializer.Serializable;

public interface LandOwner extends Serializable {
    public boolean canAccess(User user) {

    }

    public boolean canManage(User user) {

    }
}
