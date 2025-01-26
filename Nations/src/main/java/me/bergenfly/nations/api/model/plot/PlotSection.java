package me.bergenfly.nations.api.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import org.jetbrains.annotations.NotNull;

/**
 * A section of a {@link ClaimedChunk}, administrated by one {@link LandAdministrator}, and owned by one {@link LandPermissionHolder}
 * <p>
 * A PlotSection can hold ownership information.
 */
public interface PlotSection {
    @NotNull LandAdministrator getAdministrator();

    ClaimedChunk in();
}
