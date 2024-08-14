package me.bergenfly.nations.api.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;

/**
 * A section of a {@link ClaimedChunk}, owned by one {@link LandAdministrator}.
 * <p>
 * A PlotSection can hold ownership information.
 */
public interface PlotSection {
    LandAdministrator getAdministrator();
}
