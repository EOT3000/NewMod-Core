package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.model.LandAdministrator;

public class PlotSection {
    private LandAdministrator administrator;
    private ClaimedChunk chunk;

    public PlotSection(ClaimedChunk chunk, LandAdministrator administrator) {
        this.chunk = chunk;
        this.administrator = administrator;
    }

    public ClaimedChunk getChunk() {
        return chunk;
    }

    public LandAdministrator getAdministrator() {
        return administrator;
    }
}
