package me.bergenfly.nations.model;

import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.plot.ClaimedChunk;
//import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.model.plot.PlotSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * An entity, typically a government, that can claim and administrate {@link me.bergenfly.nations.model.plot.PlotSection}s.
 */
public interface LandAdministrator {
    /**
     * Gets all land that this administrator administrates. For nations, includes both nation and settlement land.
     *
     * @return a set of all the {@link PlotSection}s that this administrator administrates.
     */
    Set<PlotSection> getLand();

    PlotSection createEmptyPlotSection(@NotNull ClaimedChunk in);
}
