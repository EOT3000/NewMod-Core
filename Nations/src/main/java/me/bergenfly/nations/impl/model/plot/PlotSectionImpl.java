package me.bergenfly.nations.impl.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.PlotSection;

public class PlotSectionImpl implements PlotSection {
    private LandAdministrator administrator;

    public PlotSectionImpl(LandAdministrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public LandAdministrator getAdministrator() {
        return administrator;
    }
}
