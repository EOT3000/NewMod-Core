package me.bergenfly.nations.api.model.organization;

public interface Tribe extends Community, Organization, LandPermissionHolder, PlayerGroup, Named, Led, NationComponent {
    default boolean isSettlement() {
        return false;
    }
}
