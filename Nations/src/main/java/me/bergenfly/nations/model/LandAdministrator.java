package me.bergenfly.nations.model;

import me.bergenfly.nations.model.plot.ClaimedChunk;
//import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.serializer.Serializable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * An entity, typically a government, that can claim and administrate {@link ClaimedChunk}s.
 */
public interface LandAdministrator extends Serializable {
    /**
     * Gets all chunks that this administrator administrates, partially or fully.
     * For nations, includes both nation and settlement land.
     *
     * @return a set of all the {@link ClaimedChunk}s that this administrator administrates.
     */
    Set<ClaimedChunk> getLand();

    void addLandToList(ClaimedChunk claimedChunk);

    void removeLandFromList(ClaimedChunk claimedChunk);
}
