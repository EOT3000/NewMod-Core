package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

public interface Organization extends PlayerGroup, LandPermissionHolder, Named, Led {
    @Override
    default boolean isPartOf(User user) {
        return PlayerGroup.super.isPartOf(user);
    }
}
