package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

import java.util.Set;

public interface Settlement extends Community, Charterer, Organization, LandPermissionHolder, PlayerGroup, Named, LandAdministrator, Led, NationComponent {
    default boolean isSettlement() {
        return true;
    }
}
