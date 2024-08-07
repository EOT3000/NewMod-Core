package me.bergenfly.nations.api.model.organization;

/**
 * Represents an entity such a {@link me.bergenfly.nations.api.model.User} or {@link Organization} who can own land and hold {@link me.bergenfly.nations.api.model.permission.PlotPermission}s in a plot.
 * <p>
 * {@link LandPermissionHolder}s do not necessarily have the ability to own land in every jurisdiction ({@link LandAdministrator}).
 *
 * @see me.bergenfly.nations.api.model.plot.PlotSection
 */
public interface LandPermissionHolder extends Named {
}
