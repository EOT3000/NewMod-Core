package me.bergenfly.nations.model;

import me.bergenfly.nations.serializer.Serializable;

public interface LandOwner extends Serializable {
    boolean canAccess(User user);

    boolean canManage(User user);
}
