package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;

public interface Settlement extends PlayerGroup, Named, LandAdministrator, Led, NationComponent {

    /**
     * Sets this settlement's nation, and removes itself from the old nation.
     *
     * @param nation the nation which the settlement will be added to. {@code null} to remove the settlement from its nation.
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
        PlayerGroup.super.addMember(user);
    }
}
