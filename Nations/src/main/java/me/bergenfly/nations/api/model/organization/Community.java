package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

import java.util.Set;

public interface Community extends Organization, LandPermissionHolder, PlayerGroup, Named, Led, NationComponent {

    /**
     * Sets this community's nation, and removes itself from the old nation.
     *
     * @param nation the nation which the community will be added to. {@code null} to remove the community from its nation.
     */
    @Override
    void setNation(Nation nation);

    /**
     * Should only be used by inheritors of {@link User}.
     * <p>
     * @deprecated use {@link User#setSettlement} instead
     */
    @Deprecated
    @Override
    default void addMember(User user) {
        Organization.super.addMember(user);
    }

    long getCreationTime();

    String getFirstName();

    void addInvitation(User user);

    Set<User> getInvitations();

    boolean isSettlement();
}
