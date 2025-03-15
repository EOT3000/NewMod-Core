package me.bergenfly.nations.impl.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import org.jetbrains.annotations.NotNull;

public class PlotSectionImpl implements PlotSection {
    private LandAdministrator administrator;
    private ClaimedChunk in;

    public PlotSectionImpl(LandAdministrator administrator, ClaimedChunk in) {
        this.administrator = administrator;
        this.in = in;
    }

    @Override
    public @NotNull LandAdministrator getAdministrator() {
        return administrator;
    }

    @Override
    public ClaimedChunk in() {
        return in;
    }

    @Override
    public PlotSection cloneAt(ClaimedChunk claimedChunk) {
        return new PlotSectionImpl(administrator, claimedChunk);
    }
}
