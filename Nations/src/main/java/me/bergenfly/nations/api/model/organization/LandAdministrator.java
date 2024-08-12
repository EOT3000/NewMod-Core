package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;

import java.util.Set;

/**
 * An entity, typically a government, that can claim and administrate {@link me.bergenfly.nations.api.model.plot.PlotSection}s.
 */
public interface LandAdministrator extends Named {
    /**
     * Gets all land that this administrator administrates.
     *
     * @return a set of all the {@link PlotSection}s that this administrator administrates.
     */
    Set<PlotSection> getLand();
}
