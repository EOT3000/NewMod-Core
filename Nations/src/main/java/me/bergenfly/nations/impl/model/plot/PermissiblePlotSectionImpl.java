package me.bergenfly.nations.impl.model.plot;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;

public class PermissiblePlotSectionImpl extends PlotSectionImpl implements PermissiblePlotSection {
    public PermissiblePlotSectionImpl(LandAdministrator administrator) {
        super(administrator);
    }
}
