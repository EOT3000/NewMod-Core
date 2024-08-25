package me.bergenfly.nations.api.model.plot;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.permission.PlotPermission;

public interface PermissiblePlotSection extends PlotSection {
    boolean hasPermission(PlotPermission permission, User user);

    /**
     * Checks if a permission is set for the given permission holder.
     *
     * @param permission the permission to check for.
     * @param holder the {@link LandPermissionHolder} to check.
     * @return 1 if allowed, 0 if unset, -1 if disallowed.
     */
    byte getSetPermission(PlotPermission permission, LandPermissionHolder holder);

    /**
     * Sets a permission for the given permission holder.
     *
     * @param permission the permission to set.
     * @param holder the permission holder to set for.
     * @param allow if the permission should be allowed, or disallowed for the given permission holder.
     *              If true, then getSetPermission(permission, holder) should return 1.
     *              If false, then getSetPermission(permission, holder) should return -1.
     */
    void setPermission(PlotPermission permission, LandPermissionHolder holder, boolean allow);

    /**
     * Unsets a permission for the given permission holder.
     * The invocation of this method should result in getSetPermission(permission, holder) returning 0.
     *
     * @param permission the permission to unset.
     * @param holder the permission holder to unset the permission for.
     */
    void unsetPermission(PlotPermission permission, LandPermissionHolder holder);
}
