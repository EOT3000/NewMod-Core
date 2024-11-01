package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;

import java.util.Set;

/**
 * An entity, typically a government, that can claim and administrate {@link me.bergenfly.nations.api.model.plot.PlotSection}s.
 */
public interface LandAdministrator extends Named, LandPermissionHolder {
    /**
     * Gets all land that this administrator administrates. For nations, includes both nation and settlement land.
     *
     * @return a set of all the {@link PlotSection}s that this administrator administrates.
     */
    Set<PlotSection> getLand();

    void addLand(PlotSection section);

    void removeLand(PlotSection section);

    PlotSection createEmptyPlotSection();

    /**
     * Gets if the given user has admin permissions in land run by this administrator.
     *
     * @param user the user to check for.
     * @return true if the user is an administrator, false if not.
     */
    boolean isUserAdmin(User user);

    @Override
    default boolean isLandManager(User user) { //TODO don't know why I made two identical methods. Figure this out and simplify in the future.
        return isUserAdmin(user);
    }
}
