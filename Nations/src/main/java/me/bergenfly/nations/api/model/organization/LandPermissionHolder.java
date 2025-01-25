package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.permission.PlotPermission;

/**
 * Represents an entity such a {@link me.bergenfly.nations.api.model.User} or {@link Organization} who can own land and hold {@link PlotPermission}s in a plot.
 * <p>
 * {@link LandPermissionHolder}s do not necessarily have the ability to own land in every jurisdiction ({@link LandAdministrator}).
 *
 * @see me.bergenfly.nations.api.model.plot.PlotSection
 */
public interface LandPermissionHolder extends Named {
    /**
     * The priority of this group when it comes to a user's permission in a plot.
     * <p>
     * A {@link me.bergenfly.nations.api.model.User} will have a priority of 0, meaning it overrides all other groups.
     * <p>
     * Groups like "Ally" may have higher priorities, because they are so broad that more specific groups will override their set permission.
     *
     * @return the priority of this kind of land permission holder.
     */
    int priority();

    /**
     * Calculates the effective priority of this permission holder in a specific situation.
     * <p>
     * Holders with a 'disallow' flag should have lower priority than ones with an 'allow' flag, because a disallow should override an allow.
     *
     * @param allowed whether or not this permission holder is allowed, or not allowed to do the permission being checked.
     * @return the effective priority of this permission holder in the given context.
     */
    default int effectivePriority(boolean allowed) {
        return priority()*2 + (allowed ? 1 : 0);
    }

    boolean isPartOf(User user);
    //TODO fix this vs playergroup

    default boolean isOwnedLandManager(User user) {
        return isPartOf(user);
    }
}
